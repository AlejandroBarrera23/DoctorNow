package com.example.doctornow.model

data class Patient(
    val idType: String,
    val idNumber: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val email: String,
    val phone: String,
    val password: String
)
