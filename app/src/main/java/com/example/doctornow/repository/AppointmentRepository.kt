package com.example.doctornow.repository

import com.example.doctornow.model.Appointment

class AppointmentRepository {
    private val appointments = mutableListOf<Appointment>()

    fun getAppointmentsForPatient(patientId: String): List<Appointment> {
        return appointments.filter { it.patientId == patientId }
    }

    fun bookAppointment(appointment: Appointment): Boolean {
        if (appointments.any {
                it.date == appointment.date &&
                        it.time == appointment.time &&
                        it.doctorName == appointment.doctorName
            }) {
            return false // Horario ya ocupado
        }
        appointments.add(appointment)
        saveAppointments() // Guardar la lista de citas
        return true
    }

    fun cancelAppointment(appointment: Appointment) {
        appointments.removeIf {
            it.patientId == appointment.patientId &&
                    it.date == appointment.date &&
                    it.time == appointment.time &&
                    it.doctorName == appointment.doctorName
        }
        saveAppointments() // Actualizar la lista de citas después de cancelar
    }

    fun getAllAppointments(): List<Appointment> {
        return appointments
    }

    private fun saveAppointments() {
        // Simulación de almacenamiento persistente, aquí puedes agregar SharedPreferences o Room
        println("Citas guardadas: $appointments")
    }
}

