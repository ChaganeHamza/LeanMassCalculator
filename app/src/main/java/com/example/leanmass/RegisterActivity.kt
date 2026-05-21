package com.example.leanmass

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.leanmass.databinding.ActivityRegisterBinding
import com.example.leanmass.utils.AuthManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name  = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val pass  = binding.etPassword.text.toString()

            if (name.isBlank() || email.isBlank() || pass.isBlank()) {
                Toast.makeText(this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass.length < 6) {
                Toast.makeText(this, "Mot de passe trop court (min 6)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnRegister.isEnabled = false

            AuthManager.register(
                name, email, pass,
                onSuccess = {
                    startActivity(Intent(this, CalculatorActivity::class.java))
                    finish()
                },
                onError = { msg ->
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
            )
        }

        binding.btnGoLogin.setOnClickListener { finish() }
    }
}