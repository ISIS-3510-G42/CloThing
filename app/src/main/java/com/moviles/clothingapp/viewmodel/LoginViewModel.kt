package com.moviles.clothingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.moviles.clothingapp.model.UserData
import com.moviles.clothingapp.model.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



/*  Login ViewModel:
*   - Checks if username and password fields in view are not empty.
*   - If fields empty throws warning message.
*   - Sends the data to firebase auth (email and password only).
*   - If the user doesn't have an account, password or email is wrong or other error it throws warning msg.
*   - If authentication is successful navigates to home screen (HomeView.MainScreen.kt)
*
 */
class LoginViewModel(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome

    private val _signUpErrorMessage = MutableStateFlow<String?>(null)
    val signUpErrorMessage: StateFlow<String?> = _signUpErrorMessage

    private val _signInErrorMessage = MutableStateFlow<String?>(null)
    val signInErrorMessage: StateFlow<String?> = _signInErrorMessage

    init {
        checkUserLoggedIn()
    }


    private fun checkUserLoggedIn() {
        val user = auth.currentUser
        if (user != null) {
            _navigateToHome.value = true // Navigate to home if already logged in
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                if (email.isBlank() || password.isBlank()) {
                    _signInErrorMessage.value = "Email y contraseña no pueden estar vacíos"
                    return@launch
                }

                auth.signInWithEmailAndPassword(email, password) // Sign in with Firebase
                    .await()

                _navigateToHome.value = true // Navigate to the home screen


            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is FirebaseAuthInvalidCredentialsException ->
                        "Correo o contraseña incorrectos"

                    else ->
                        "Error al iniciar sesión: ${e.localizedMessage ?: "Intenta de nuevo más tarde"}"
                }
                _signInErrorMessage.value = errorMessage
            }
        }
    }


    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                if (email.isBlank() || password.isBlank()) {
                    _signUpErrorMessage.value = "Email y contraseña no pueden estar vacíos"
                    return@launch
                }

                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user


                if (firebaseUser != null) {
                    val userData = UserData(
                        id = 0,  // or 0 if backend auto-generates it
                        name = email.substringBefore("@"),
                        email = email,
                        postedProducts = "[]",
                        boughtProducts = "[]"
                    )

                    val createdUser = userRepository.createUser(userData)

                    if (createdUser == null) {
                        _signUpErrorMessage.value = "Error al registrar usuario en el backend"
                        return@launch
                    }

                    _navigateToHome.value = true

                } else {
                    _signUpErrorMessage.value = "No se pudo obtener el usuario de Firebase"
                }

            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is FirebaseAuthUserCollisionException ->
                        "Este correo ya está registrado"
                    is FirebaseAuthWeakPasswordException ->
                        "La contraseña es muy débil: ${e.reason}"
                    is FirebaseAuthInvalidCredentialsException ->
                        "Formato de correo inválido"
                    else -> "Error al crear cuenta: ${e.localizedMessage ?: "Error desconocido"}"
                }
                _signUpErrorMessage.value = errorMessage
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _navigateToHome.value = false
    }

    fun setPasswordMismatchError() {
        _signUpErrorMessage.value = "Las contraseñas no coinciden"
    }

    fun clearSignUpError() {
        _signUpErrorMessage.value = null
    }


    fun onHomeNavigated() {
        _navigateToHome.value = false
    }
}