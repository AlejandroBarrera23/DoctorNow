package com.example.doctornow.view

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.doctornow.R
import com.example.doctornow.viewmodel.LoginAdminViewModel

class LoginAdminActivity : AppCompatActivity() {

    private val viewModel: LoginAdminViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_admin)

        val etUsername = findViewById<EditText>(R.id.et_admin_username)
        val etPassword = findViewById<EditText>(R.id.et_admin_password)
        val btnLogin = findViewById<Button>(R.id.btn_admin_login)
        val btnBack = findViewById<Button>(R.id.btn_admin_back)
        btnBack.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la anterior
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            viewModel.loginAdmin(username, password)
        }

        viewModel.loginStatus.observe(this, Observer { isAuthenticated ->
            if (isAuthenticated) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminDashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
