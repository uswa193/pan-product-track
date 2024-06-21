package com.capstone.pan.view.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.pan.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    fun getUserSession() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getProfile().collect { userModel ->
                _userName.value = userModel.name
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
