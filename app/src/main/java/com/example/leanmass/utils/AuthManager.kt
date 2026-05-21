// utils/AuthManager.kt
package com.example.leanmass.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object AuthManager {

    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()

    fun isLoggedIn(): Boolean = auth.currentUser != null

    fun register(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user!!.uid
                // Save extra user info (name) in Firestore
                val userMap = mapOf("name" to name, "email" to email)
                db.collection("users").document(uid).set(userMap)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onError(it.message ?: "Firestore error") }
            }
            .addOnFailureListener { onError(it.message ?: "Auth error") }
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

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid
}