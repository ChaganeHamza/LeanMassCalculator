package com.example.leanmass.data

data class LbmRecord(
    val id: Long = 0,
    val userId: Long = 0,
    val date: String = "",
    val gender: String = "",
    val weight: Float = 0f,
    val height: Float = 0f,
    val lbm: Float = 0f,
    val isSatisfactory: Boolean = false
)