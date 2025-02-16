package com.example.doctornow.view

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.doctornow.R
import com.example.doctornow.viewmodel.LoginUserViewModel

class LoginUserActivity : AppCompatActivity() {

    private val viewModel: LoginUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)

        val etUserId = findViewById<EditText>(R.id.et_user_id)
        val etPassword = findViewById<EditText>(R.id.et_user_password)
        val btnLogin = findViewById<Button>(R.id.btn_user_login)
        val btnBack = findViewById<Button>(R.id.btn_admin_back)

        btnBack.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la anterior
        }

        btnLogin.setOnClickListener {
            val idNumber = etUserId.text.toString()
            val password = etPassword.text.toString()

            if (idNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, ingresa tu identificación y contraseña", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.loginPatient(idNumber, password)
        }

        viewModel.loginStatus.observe(this, Observer { isAuthenticated ->
            if (isAuthenticated) {
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                // Obtener el nombre del paciente
                val patientName = viewModel.getPatientName(etUserId.text.toString())

                // Redirigir al Dashboard del paciente con su ID y nombre
                val intent = Intent(this, PatientDashboardActivity::class.java)
                intent.putExtra("patientId", etUserId.text.toString())
                intent.putExtra("patientName", patientName)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
