package com.example.leanmass.data

data class UserRecord(
    val id: Long,
    val fullName: String,
    val email: String,
    val passwordHash: String
)