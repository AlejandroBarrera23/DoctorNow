package com.example.doctornow.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.doctornow.R
import com.example.doctornow.model.Appointment
import com.example.doctornow.repository.DoctorRepository
import com.example.doctornow.viewmodel.AppointmentViewModel
import java.text.SimpleDateFormat
import java.util.*

class PatientDashboardActivity : AppCompatActivity() {

    private val viewModel: AppointmentViewModel by viewModels()
    private lateinit var patientId: String
    private lateinit var patientName: String
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_dashboard)

        // Obtener datos del paciente desde el Intent
        patientId = intent.getStringExtra("patientId") ?: ""
        patientName = intent.getStringExtra("patientName") ?: "Paciente"

        // Actualizar el nombre del paciente en la interfaz
        val tvPatientName = findViewById<TextView>(R.id.tv_patient_name)
        tvPatientName.text = "Bienvenido, $patientName"

        val btnLogout = findViewById<Button>(R.id.btn_logout)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val spinnerSpecialty = findViewById<Spinner>(R.id.spinner_specialty)
        val spinnerDoctor = findViewById<Spinner>(R.id.spinner_doctor)
        val spinnerTime = findViewById<Spinner>(R.id.spinner_time)
        val btnSchedule = findViewById<Button>(R.id.btn_schedule_appointment)
        val btnViewAppointments = findViewById<Button>(R.id.btn_view_appointments)

        // Configurar cierre de sesión
        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginUserActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Cargar especialidades y médicos desde DoctorRepository
        val doctorRepository = DoctorRepository()
        val specialties = mutableListOf("Selecciona una especialidad")
        specialties.addAll(doctorRepository.getSpecialties())
        val specialtyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, specialties)
        spinnerSpecialty.adapter = specialtyAdapter

        spinnerSpecialty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                if (position == 0) return
                val selectedSpecialty = specialties[position]
                val doctors = mutableListOf("Selecciona un médico")
                doctors.addAll(doctorRepository.getDoctorsBySpecialty(selectedSpecialty))
                val doctorAdapter = ArrayAdapter(this@PatientDashboardActivity, android.R.layout.simple_spinner_dropdown_item, doctors)
                spinnerDoctor.adapter = doctorAdapter
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Obtener la fecha actual para comparación
        val currentDate = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Manejar la selección del calendario
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, dayOfMonth)

            // Evitar que se seleccione una fecha anterior a hoy
            if (selectedCalendar.before(currentDate)) {
                Toast.makeText(this, "No puedes seleccionar una fecha pasada.", Toast.LENGTH_SHORT).show()
                spinnerSpecialty.isEnabled = false
                spinnerDoctor.isEnabled = false
                spinnerTime.isEnabled = false
                btnSchedule.isEnabled = false
                return@setOnDateChangeListener
            }

            selectedDate = dateFormatter.format(selectedCalendar.time)
            spinnerSpecialty.isEnabled = true
            spinnerDoctor.isEnabled = true
            spinnerTime.isEnabled = true
            btnSchedule.isEnabled = true
            cargarHorasDisponibles()
        }

        btnSchedule.setOnClickListener {
            val doctorName = spinnerDoctor.selectedItem.toString()
            val specialty = spinnerSpecialty.selectedItem.toString()
            val time = spinnerTime.selectedItem.toString()

            if (selectedDate.isEmpty() || specialty == "Selecciona una especialidad" || doctorName == "Selecciona un médico" || time == "Selecciona una hora") {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val appointment = Appointment(patientId, doctorName, specialty, selectedDate, time)
            viewModel.bookAppointment(appointment)
        }

        btnViewAppointments.setOnClickListener {
            viewModel.loadAppointmentsForPatient(patientId) // Cargar citas antes de abrir la pantalla
            val intent = Intent(this, ViewAppointmentsActivity::class.java)
            intent.putExtra("patientId", patientId)
            startActivity(intent)
        }

        // Observar cambios en el estado de la cita
        viewModel.appointmentStatus.observe(this, Observer { isBooked ->
            if (isBooked) {
                Toast.makeText(this, "Cita agendada con éxito", Toast.LENGTH_SHORT).show()
                resetSelections()
                viewModel.loadAppointmentsForPatient(patientId) // Asegurar que se actualizan las citas
                val intent = Intent(this, ViewAppointmentsActivity::class.java)
                intent.putExtra("patientId", patientId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "El horario ya está ocupado", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarHorasDisponibles() {
        val workStartHour = 8  // 08:00 AM
        val workEndHour = 17   // 05:00 PM
        val intervalMinutes = 30 // Intervalo de 30 minutos

        val availableTimes = mutableListOf("Selecciona una hora")
        val calendar = Calendar.getInstance()

        for (hour in workStartHour until workEndHour) {
            for (minute in arrayOf(0, intervalMinutes)) {
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                availableTimes.add(timeFormat.format(calendar.time))
            }
        }

        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, availableTimes)
        findViewById<Spinner>(R.id.spinner_time).adapter = timeAdapter
    }

    private fun resetSelections() {
        findViewById<Spinner>(R.id.spinner_specialty).setSelection(0)
        findViewById<Spinner>(R.id.spinner_doctor).setSelection(0)
        findViewById<Spinner>(R.id.spinner_time).setSelection(0)
        selectedDate = ""
    }
}



