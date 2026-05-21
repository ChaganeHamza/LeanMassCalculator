package com.example.leanmass

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.leanmass.databinding.ActivityLoginBinding
import com.example.leanmass.utils.AuthManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If already logged in, skip to main screen
        if (AuthManager.isLoggedIn()) {
            startActivity(Intent(this, CalculatorActivity::class.java))
            finish(); return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass  = binding.etPassword.text.toString()

            if (email.isBlank() || pass.isBlank()) {
                Toast.makeText(this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnLogin.isEnabled = false // prevent double tap

            AuthManager.login(
                email, pass,
                onSuccess = {
                    startActivity(Intent(this, CalculatorActivity::class.java))
                    finish()
                },
                onError = { msg ->
                    binding.btnLogin.isEnabled = true
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            )
        }

        binding.btnGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}