package com.example.doctornow.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.doctornow.R
import com.example.doctornow.viewmodel.AppointmentViewModel

class ViewAppointmentsActivity : AppCompatActivity() {

    private val viewModel: AppointmentViewModel by viewModels()
    private lateinit var patientId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_appointments)

        patientId = intent.getStringExtra("patientId") ?: ""

        val listViewAppointments = findViewById<ListView>(R.id.listView_appointments)
        val tvNoAppointments = findViewById<TextView>(R.id.tv_no_appointments)
        val btnCancelAppointment = findViewById<Button>(R.id.btn_cancel_appointment)
        val btnBack = findViewById<Button>(R.id.btn_back)

        viewModel.loadAppointmentsForPatient(patientId)

        viewModel.appointments.observe(this, Observer { appointments ->
            if (appointments.isEmpty()) {
                tvNoAppointments.visibility = View.VISIBLE
                listViewAppointments.visibility = View.GONE
            } else {
                tvNoAppointments.visibility = View.GONE
                listViewAppointments.visibility = View.VISIBLE

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice,
                    appointments.map { "${it.date} - ${it.time} | ${it.doctorName} (${it.specialty})" })

                listViewAppointments.choiceMode = ListView.CHOICE_MODE_SINGLE
                listViewAppointments.adapter = adapter
            }
        })



        // Cancelar cita seleccionada
        btnCancelAppointment.setOnClickListener {
            val position = listViewAppointments.checkedItemPosition
            if (position != ListView.INVALID_POSITION) {
                val selectedAppointment = viewModel.appointments.value?.get(position)
                selectedAppointment?.let {
                    viewModel.cancelAppointment(it)
                    Toast.makeText(this, "Cita cancelada", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Selecciona una cita para cancelar", Toast.LENGTH_SHORT).show()
            }
        }


        btnBack.setOnClickListener {
            finish()
        }
    }
}

