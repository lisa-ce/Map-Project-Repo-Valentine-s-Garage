package com.example.mapprojectvalentinesgaragemanagementsystem.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun signUp(
        fullName: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = auth.currentUser?.uid ?: return@addOnSuccessListener

                val role = if (email.lowercase() == "valentine@gmail.com") {
                    "Owner"
                } else {
                    "Mechanic"
                }

                val user = hashMapOf(
                    "fullName" to fullName,
                    "email" to email,
                    "role" to role
                )

                db.collection("users")
                    .document(userId)
                    .set(user)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onError(it.message ?: "Failed to save user") }
            }
            .addOnFailureListener {
                onError(it.message ?: "Sign up failed")
            }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Login failed") }
    }

    fun getCurrentUserRole(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            onError("No logged in user")
            return
        }

        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val role = document.getString("role") ?: "Mechanic"
                onSuccess(role)
            }
            .addOnFailureListener {
                onError(it.message ?: "Failed to get user role")
            }
    }

    fun logout() {
        auth.signOut()
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}