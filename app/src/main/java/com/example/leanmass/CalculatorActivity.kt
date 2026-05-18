package com.example.leanmass

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.leanmass.data.DatabaseHelper
import com.example.leanmass.data.LbmRecord
import com.example.leanmass.databinding.ActivityCalculatorBinding
import com.example.leanmass.utils.AuthManager
import com.example.leanmass.utils.LbmCalculator

class CalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)

        binding.btnCalculate.setOnClickListener { calculate() }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            AuthManager.logout(this)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun calculate() {
        val weightStr = binding.etWeight.text.toString()
        val heightStr = binding.etHeight.text.toString()
        if (weightStr.isBlank() || heightStr.isBlank()) {
            Toast.makeText(this, "Remplissez poids et taille", Toast.LENGTH_SHORT).show()
            return
        }
        val weight = weightStr.toFloatOrNull()
        val height = heightStr.toFloatOrNull()
        if (weight == null || height == null || weight <= 0 || height <= 0) {
            Toast.makeText(this, "Valeurs invalides", Toast.LENGTH_SHORT).show()
            return
        }
        val selectedId = binding.radioGroupGender.checkedRadioButtonId
        val gender = findViewById<RadioButton>(selectedId).text.toString()
        val lbm = LbmCalculator.calculate(gender, weight, height)
        val ok  = LbmCalculator.isSatisfactory(gender, lbm)

        db.insertRecord(LbmRecord(
            userId = AuthManager.currentUserId(this),
            gender = gender, weight = weight,
            height = height, lbm = lbm, isSatisfactory = ok
        ))

        startActivity(Intent(this, ResultActivity::class.java).apply {
            putExtra("LBM", lbm)
            putExtra("OK", ok)
            putExtra("GENDER", gender)
            putExtra("WEIGHT", weight)
            putExtra("HEIGHT", height)
        })
    }
}