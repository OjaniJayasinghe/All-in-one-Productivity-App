package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        // Load saved username and password if available
        loadLoginData()

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateLogin(username, password)) {
                saveLoginData(username, password)
                goToHomePage()
            }
        }
    }

    private fun validateLogin(username: String, password: String): Boolean {
        return when {
            username.isEmpty() -> {
                etUsername.error = "Username is required"
                false
            }
            password.isEmpty() -> {
                etPassword.error = "Password is required"
                false
            }
            username == "admin" && password == "1234" -> {
                true // Valid login credentials
            }
            else -> {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    private fun saveLoginData(username: String, password: String) {
        // Save login information using Shared Preferences
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Username", username)
        editor.putString("Password", password)
        editor.apply()
    }

    private fun loadLoginData() {
        // Load saved login data if available
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("Username", "")
        val savedPassword = sharedPreferences.getString("Password", "")

        etUsername.setText(savedUsername)
        etPassword.setText(savedPassword)
    }

    private fun goToHomePage() {
        // Navigate to HomeActivity after successful login
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
        finish() // Close the login screen
    }

}
