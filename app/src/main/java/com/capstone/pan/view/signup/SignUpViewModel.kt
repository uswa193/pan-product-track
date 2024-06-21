package com.capstone.pan.view.signup

import androidx.lifecycle.ViewModel
import com.capstone.pan.data.UserRepository
import com.capstone.pan.data.retrofit.response.RegisterResponse
import com.capstone.pan.di.Result
import kotlinx.coroutines.flow.Flow

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    fun userRegister(username: String, email: String, password: String): Flow<Result<RegisterResponse>> {
        return repository.userRegister(username, email, password)
    }
}