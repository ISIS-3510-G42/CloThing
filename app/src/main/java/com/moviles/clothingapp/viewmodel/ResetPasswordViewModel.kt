package com.moviles.clothingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth

/* Reset Password ViewModel: sends the request to firebase and updates status for view to update page. */
class ResetPasswordViewModel(private val auth: FirebaseAuth) : ViewModel() {

    private val _resetPasswordResult = MutableLiveData<ResetPasswordResult>()
    val resetPasswordResult: LiveData<ResetPasswordResult> get() = _resetPasswordResult

    fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _resetPasswordResult.value = ResetPasswordResult.Success
                } else {
                    _resetPasswordResult.value = ResetPasswordResult.Failure(task.exception?.message ?: "error")
                }
            }
    }

    sealed class ResetPasswordResult {
        object Success : ResetPasswordResult()
        data class Failure(val errorMessage: String) : ResetPasswordResult()
    }


}