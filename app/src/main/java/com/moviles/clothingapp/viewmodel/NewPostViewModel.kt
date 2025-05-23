package com.moviles.clothingapp.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.clothingapp.model.ImageStoringRepository
import com.moviles.clothingapp.model.PostData
import com.moviles.clothingapp.repository.PostRepository
import kotlinx.coroutines.launch




/*  Create a new post ViewModel: used to see changes in state of the View
*   Gets the information inserted by the user in the CreatePostScreen and CameraScreen
*   Sends the information gotten by UI to the repository to POST the information into backend and BD
*/
class NewPostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PostRepository()
    private val appwriteRepository = ImageStoringRepository(application.applicationContext)

    /* Title field */
    private val _title = mutableStateOf("")
    val title: State<String> = _title

    /* Brand field */
    private val _brand = mutableStateOf("")
    val brand: State<String> = _brand

    /* Dropdown selections */
    /* Size selection */
    private val _selectedSize = mutableStateOf("M")
    val selectedSize: State<String> = _selectedSize

    /* Category selection */
    private val _selectedCategory = mutableStateOf("Calor")
    val selectedCategory: State<String> = _selectedCategory

    /* Group selection */
    private val _selectedGroup = mutableStateOf("Hombre")
    val selectedGroup: State<String> = _selectedGroup

    /* Color selection */
    private val _selectedColor = mutableStateOf("Negro")
    val selectedColor: State<String> = _selectedColor

    /* Price selection */
    private val _price = mutableStateOf("")
    val price: State<String> = _price

    /* Image taken by user */
    private val _imageUri = mutableStateOf<String?>(null)
    val imageUri: State<String?> = _imageUri


    /* Functions to update the state of fields */
    fun setTitle(newTitle: String) { _title.value = newTitle }
    fun setBrand(brand: String) { _brand.value = brand }
    fun setSize(size: String) { _selectedSize.value = size }
    fun setCategory(category: String) { _selectedCategory.value = category }
    fun setGroup(group: String) { _selectedGroup.value = group }
    fun setColor(color: String) { _selectedColor.value = color }
    fun setPrice(newPrice: String) { _price.value = newPrice }
    fun setImage(uri: String?) {
        _imageUri.value = uri
    }


    /* Submit Post function to send the data gotten by UI to the repository */
    fun submitPost(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {

                val context = getApplication<Application>().applicationContext
                val bucketId = "67ddf3860035ee6bd725"

                // Upload Image First
                val imageUri = _imageUri.value?.let { Uri.parse(it) }  // Convert String to Uri
                val imageUrl = imageUri?.let { appwriteRepository.uploadImage(context, it, bucketId) }


                if (imageUrl == null) {
                    onResult(false)
                    return@launch
                }


                val newPost = PostData(
                    name = title.value,
                    brand = brand.value,
                    size = selectedSize.value,
                    category = selectedCategory.value,
                    group = selectedGroup.value,
                    price = price.value,
                    image = imageUrl,
                    color = selectedColor.value
                )

                val response = repository.createPost(newPost)
                onResult(response != null)  // Notify success or failure
            } catch (e: Exception) {
                e.printStackTrace()  // Log error for debugging
                onResult(false)  // Notify failure
            }
        }
    }
}
