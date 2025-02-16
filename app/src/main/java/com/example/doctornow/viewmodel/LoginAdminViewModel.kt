package com.example.doctornow.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.doctornow.repository.AdminRepository

class LoginAdminViewModel : ViewModel() {

    private val repository = AdminRepository()

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    fun loginAdmin(username: String, password: String) {
        _loginStatus.value = repository.authenticateAdmin(username, password)
    }
}
