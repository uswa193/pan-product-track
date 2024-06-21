package com.capstone.pan.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.pan.data.pref.UserModel
import com.capstone.pan.data.retrofit.response.LoginResponse
import com.capstone.pan.di.Result
import com.capstone.pan.data.UserRepository
import com.capstone.pan.data.pref.UserModelRegist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun userLogin(email: String, password: String): Flow<Result<LoginResponse>> {
        return repository.userLogin(email, password)
            .catch { e ->
                emit(Result.Error("Failed to login: ${e.message ?: "Unknown error"}"))
            }
            .flowOn(Dispatchers.IO)
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    fun saveSession1(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
