package com.example.doctornow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doctornow.repository.DoctorRepository
import com.example.doctornow.repository.Doctor

class DoctorViewModel : ViewModel() {
    private val repository = DoctorRepository()

    private val _specialties = MutableLiveData<List<String>>()
    val specialties: LiveData<List<String>> get() = _specialties

    private val _doctorsBySpecialty = MutableLiveData<List<String>>()
    val doctorsBySpecialty: LiveData<List<String>> get() = _doctorsBySpecialty

    fun loadSpecialties() {
        _specialties.value = repository.getSpecialties()
    }

    fun loadDoctorsBySpecialty(specialty: String) {
        _doctorsBySpecialty.value = repository.getDoctorsBySpecialty(specialty)
    }
}

