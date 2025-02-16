package com.example.doctornow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doctornow.model.Appointment
import com.example.doctornow.repository.AppointmentRepository

class AppointmentViewModel : ViewModel() {
    private val repository = AppointmentRepository()

    private val _appointments = MutableLiveData<List<Appointment>>()
    val appointments: LiveData<List<Appointment>> get() = _appointments

    private val _appointmentStatus = MutableLiveData<Boolean>()
    val appointmentStatus: LiveData<Boolean> get() = _appointmentStatus

    fun loadAppointmentsForPatient(patientId: String) {
        _appointments.value = repository.getAppointmentsForPatient(patientId)
    }

    fun bookAppointment(appointment: Appointment) {
        val success = repository.bookAppointment(appointment)
        if (success) {
            _appointments.value = repository.getAppointmentsForPatient(appointment.patientId)
        }
        _appointmentStatus.value = success
    }

    fun cancelAppointment(appointment: Appointment) {
        repository.cancelAppointment(appointment)
        _appointments.value = repository.getAppointmentsForPatient(appointment.patientId)
    }

    private val _filteredAppointments = MutableLiveData<List<Appointment>>()
    val filteredAppointments: LiveData<List<Appointment>> get() = _filteredAppointments

    fun loadAppointmentsFiltered(date: String, doctorName: String) {
        val allAppointments = repository.getAllAppointments()
        val filtered = allAppointments.filter {
            (date.isEmpty() || it.date == date) && (doctorName == "Todos los médicos" || it.doctorName == doctorName)
        }
        _filteredAppointments.value = filtered
    }

}

