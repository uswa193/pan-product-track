package com.dicoding.picodiploma.pan.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.pan.data.UserRepository
import com.dicoding.picodiploma.pan.data.retrofit.response.RegisterResponse
import com.dicoding.picodiploma.pan.di.Result
import kotlinx.coroutines.flow.Flow

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    fun userRegister(name: String, email: String, password: String): Flow<Result<RegisterResponse>> {
        return repository.userRegister(name, email, password)
    }
}