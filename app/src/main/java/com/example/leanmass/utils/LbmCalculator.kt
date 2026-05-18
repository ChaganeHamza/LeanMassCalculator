package com.example.leanmass.utils

object LbmCalculator {
    fun calculate(gender: String, weightKg: Float, heightCm: Float): Float {
        return if (gender == "Homme") {
            (0.407f * weightKg) + (0.267f * heightCm) - 19.2f
        } else {
            (0.252f * weightKg) + (0.473f * heightCm) - 48.3f
        }
    }

    fun isSatisfactory(gender: String, lbm: Float): Boolean {
        val threshold = if (gender == "Homme") 38f else 24f
        return lbm >= threshold
    }
}