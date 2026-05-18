package com.example.leanmass

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.leanmass.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lbm = intent.getFloatExtra("LBM", 0f)
        val ok  = intent.getBooleanExtra("OK", false)

        binding.tvLbmValue.text = "%.1f kg".format(lbm)

        if (ok) {
            binding.tvStatusText.text = "Résultat satisfaisant"
            binding.tvStatusText.setTextColor(0xFF1D9E75.toInt())
            binding.ivStatus.setImageResource(android.R.drawable.checkbox_on_background)
        } else {
            binding.tvStatusText.text = "Résultat à surveiller"
            binding.tvStatusText.setTextColor(0xFFBA7517.toInt())
            binding.ivStatus.setImageResource(android.R.drawable.ic_dialog_alert)
        }

        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        binding.btnNewCalc.setOnClickListener { finish() }
    }
}