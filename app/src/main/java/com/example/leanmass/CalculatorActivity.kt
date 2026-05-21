package com.example.leanmass

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.leanmass.databinding.ActivityCalculatorBinding
import com.example.leanmass.utils.AuthManager
import com.example.leanmass.utils.LbmCalculator
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class CalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalculate.setOnClickListener { calculate() }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            AuthManager.logout()                              // ✅ no param
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

        // ✅ Save to Firestore instead of SQLite
        val uid = AuthManager.getCurrentUserId()
        if (uid != null) {
            val record = hashMapOf(
                "userId"          to uid,
                "gender"          to gender,
                "weight"          to weight,
                "height"          to height,
                "lbm"             to lbm,
                "isSatisfactory"  to ok,
                "date"            to Date()
            )
            firestore.collection("records").add(record)
                .addOnFailureListener {
                    Toast.makeText(this, "Erreur sauvegarde: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        startActivity(Intent(this, ResultActivity::class.java).apply {
            putExtra("LBM", lbm)
            putExtra("OK", ok)
            putExtra("GENDER", gender)
            putExtra("WEIGHT", weight)
            putExtra("HEIGHT", height)
        })
    }
}