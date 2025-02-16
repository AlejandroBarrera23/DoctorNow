package com.example.doctornow.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.doctornow.model.Patient

class PatientRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("patient_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    fun savePatient(patient: Patient) {
        val patientsList = getAllPatients().toMutableList()
        patientsList.add(patient)
        val json = gson.toJson(patientsList)
        sharedPreferences.edit().putString("patients", json).apply()
    }

    fun getAllPatients(): List<Patient> {
        val json = sharedPreferences.getString("patients", null) ?: return emptyList()
        val type = object : TypeToken<List<Patient>>() {}.type
        return gson.fromJson(json, type)
    }

    fun authenticatePatient(idNumber: String, password: String): Boolean {
        val patients = getAllPatients()
        return patients.any { it.idNumber == idNumber && it.password == password }
    }

    fun getPatientNameById(idNumber: String): String {
        val patients = getAllPatients()
        return patients.find { it.idNumber == idNumber }?.firstName ?: "Paciente"
    }

}


