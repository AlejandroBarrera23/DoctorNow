package com.example.doctornow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.doctornow.model.Patient
import com.example.doctornow.repository.PatientRepository

class RegisterUserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PatientRepository(application)

    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus

    fun registerPatient(patient: Patient) {
        if (patient.idNumber.isBlank() || patient.firstName.isBlank() || patient.lastName.isBlank() ||
            patient.birthDate.isBlank() || patient.email.isBlank() || patient.phone.isBlank() ||
            patient.password.isBlank()
        ) {
            _registrationStatus.value = false
            return
        }

        repository.savePatient(patient)
        _registrationStatus.value = true
    }
}
