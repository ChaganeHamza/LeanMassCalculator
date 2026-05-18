package com.example.leanmass.utils

import android.content.Context
import com.example.leanmass.data.DatabaseHelper
import java.security.MessageDigest

object AuthManager {

    private const val PREFS = "leanmass_prefs"
    private const val KEY_USER_ID = "logged_user_id"

    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun register(context: Context, db: DatabaseHelper,
                 name: String, email: String, password: String): Boolean {
        if (db.getUserByEmail(email) != null) return false
        val id = db.insertUser(name, email, hashPassword(password))
        if (id > 0) { saveSession(context, id); return true }
        return false
    }

    fun login(context: Context, db: DatabaseHelper,
              email: String, password: String): Boolean {
        val user = db.getUserByEmail(email) ?: return false
        if (user.passwordHash != hashPassword(password)) return false
        saveSession(context, user.id)
        return true
    }

    fun logout(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().remove(KEY_USER_ID).apply()
    }

    fun currentUserId(context: Context): Long =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getLong(KEY_USER_ID, -1L)

    fun isLoggedIn(context: Context) = currentUserId(context) != -1L

    private fun saveSession(context: Context, userId: Long) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putLong(KEY_USER_ID, userId).apply()
    }
}