package com.example.doctornow.repository

import com.example.doctornow.model.AdminUser
import com.example.doctornow.model.Appointment

class AdminRepository {

    // Lista de usuarios predefinidos
    private val adminUsers = listOf(
        AdminUser("administrador", "12345"),
        AdminUser("recepcionista1", "12345"),
        AdminUser("recepcionista2", "12345")
    )

    fun authenticateAdmin(username: String, password: String): Boolean {
        return adminUsers.any { it.username == username && it.password == password }
    }
}
