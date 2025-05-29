package com.moviles.clothingapp.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

class UserRepository {

    private val BASE_URL = "http://10.0.2.2:8000/" // Emulator localhost

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    private var currentUserEmail: String? = null

    suspend fun fetchUserByEmail(userEmail: String): UserData? {
        return try {
            val response = apiService.fetchUserByEmail(userEmail)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("UserRepository", "Error fetching user by email: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception: ${e.message}")
            null
        }
    }

    suspend fun createUser(userData: UserData): UserData? {
        return try {
            val response = apiService.createUser(userData)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("UserRepository", "Error creating user: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception: ${e.message}")
            null
        }
    }

    suspend fun updateUserProducts(userEmail: String, postedProducts: String?, boughtProducts: String?): UserData? {
        val updateMap = mutableMapOf<String, String?>()
        postedProducts?.let { updateMap["postedProducts"] = it }
        boughtProducts?.let { updateMap["boughtProducts"] = it }

        return try {
            val response = apiService.updateUserByEmail(userEmail, updateMap)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("UserRepository", "Error updating user: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception: ${e.message}")
            null
        }
    }

    fun getCurrentUserEmail(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }

    interface ApiService {
        @GET("users/email/{userEmail}")
        suspend fun fetchUserByEmail(@Path("userEmail") userEmail: String): Response<UserData>

        @POST("create-user/")
        suspend fun createUser(@Body userData: UserData): Response<UserData>

        @PUT("users/email/{userEmail}")
        suspend fun updateUserByEmail(
            @Path("userEmail") userEmail: String,
            @Body updates: Map<String, String?>
        ): Response<UserData>
    }
}
