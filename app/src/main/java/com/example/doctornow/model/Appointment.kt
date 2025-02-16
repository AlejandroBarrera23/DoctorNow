package com.example.doctornow.model

data class Appointment(
    val patientId: String,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String
)
