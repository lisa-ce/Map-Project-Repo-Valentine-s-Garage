package com.example.mapprojectvalentinesgaragemanagementsystem.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mapprojectvalentinesgaragemanagementsystem.data.repository.AuthRepository

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    fun signUp(
        fullName: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        repository.signUp(fullName, email, password, onSuccess, onError)
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        repository.login(email, password, onSuccess, onError)
    }

    fun getCurrentUserRole(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        repository.getCurrentUserRole(onSuccess, onError)
    }

    fun isLoggedIn(): Boolean {
        return repository.isLoggedIn()
    }

    fun logout() {
        repository.logout()
    }
}