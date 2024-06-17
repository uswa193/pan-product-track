package com.dicoding.picodiploma.pan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.pan.data.UserRepository
import com.dicoding.picodiploma.pan.data.pref.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): Flow<UserModel> {
        return repository.getSession()
    }



    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}