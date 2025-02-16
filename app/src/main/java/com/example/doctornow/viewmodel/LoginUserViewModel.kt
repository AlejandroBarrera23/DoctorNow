package com.example.doctornow.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.doctornow.repository.PatientRepository

class LoginUserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PatientRepository(application)

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    fun loginPatient(idNumber: String, password: String) {
        _loginStatus.value = repository.authenticatePatient(idNumber, password)
    }
    fun getPatientName(idNumber: String): String {
        return repository.getPatientNameById(idNumber)
    }

}
