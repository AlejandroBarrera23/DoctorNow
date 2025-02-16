package com.example.doctornow.view

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.doctornow.R
import com.example.doctornow.model.Patient
import com.example.doctornow.viewmodel.RegisterUserViewModel

class RegisterUserActivity : AppCompatActivity() {

    private val viewModel: RegisterUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        val spinnerIdType = findViewById<Spinner>(R.id.spinner_id_type)
        val etUserId = findViewById<EditText>(R.id.et_user_id)
        val etFirstName = findViewById<EditText>(R.id.et_first_name)
        val etLastName = findViewById<EditText>(R.id.et_last_name)
        val etBirthDate = findViewById<EditText>(R.id.et_birth_date)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPhone = findViewById<EditText>(R.id.et_phone)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val etConfirmPassword = findViewById<EditText>(R.id.et_confirm_password)
        val btnRegister = findViewById<Button>(R.id.btn_register_user)
        val btnBack = findViewById<Button>(R.id.btn_admin_back)

        btnBack.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la anterior
        }

        btnRegister.setOnClickListener {
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "La contraseña no puede estar vacía", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val patient = Patient(
                idType = spinnerIdType.selectedItem.toString(),
                idNumber = etUserId.text.toString(),
                firstName = etFirstName.text.toString(),
                lastName = etLastName.text.toString(),
                birthDate = etBirthDate.text.toString(),
                email = etEmail.text.toString(),
                phone = etPhone.text.toString(),
                password = password
            )

            viewModel.registerPatient(patient)
        }

        viewModel.registrationStatus.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Paciente registrado con éxito", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error en el registro. Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

