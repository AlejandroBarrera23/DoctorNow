package com.example.doctornow.repository

class DoctorRepository {
    private val doctors = listOf(
        Doctor("Dr. Juan Pérez", "Cardiología"),
        Doctor("Dra. Ana Gómez", "Cardiología"),
        Doctor("Dr. Carlos Ramírez", "Dermatología"),
        Doctor("Dra. Sofía Fernández", "Dermatología"),
        Doctor("Dr. Manuel Herrera", "Pediatría"),
        Doctor("Dra. Laura Martínez", "Pediatría")
    )

    fun getSpecialties(): List<String> {
        return doctors.map { it.specialty }.distinct()
    }

    fun getDoctorsBySpecialty(specialty: String): List<String> {
        return doctors.filter { it.specialty == specialty }.map { it.name }
    }

    fun getAllDoctors(): List<Doctor> {
        return doctors
    }
}

data class Doctor(val name: String, val specialty: String)
