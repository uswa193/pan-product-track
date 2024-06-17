package com.dicoding.picodiploma.pan.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.pan.data.UserRepository
import com.dicoding.picodiploma.pan.data.pref.UserModel
import com.dicoding.picodiploma.pan.data.retrofit.response.LoginResponse
import com.dicoding.picodiploma.pan.di.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun userLogin(email: String, password: String): Flow<Result<LoginResponse>> {
        return repository.userLogin(email, password)
    }
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}