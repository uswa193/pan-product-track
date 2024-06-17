package com.dicoding.picodiploma.pan.data

import com.dicoding.picodiploma.pan.data.pref.UserModel
import com.dicoding.picodiploma.pan.data.pref.UserPreference
import com.dicoding.picodiploma.pan.data.retrofit.api.ApiService
import com.dicoding.picodiploma.pan.data.retrofit.response.AddJournalResponse
import com.dicoding.picodiploma.pan.data.retrofit.response.LoginResponse
import com.dicoding.picodiploma.pan.data.retrofit.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.dicoding.picodiploma.pan.di.Result
import kotlinx.coroutines.flow.flowOn
import java.util.Date

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private var apiService: ApiService,

    ) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun userLogin(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.userLogin(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun userRegister(
        name: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponse>> = flow {
        try {
            val response = apiService.userRegister(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun addJournal(
        date: Date,
        emotion: String,
        note: String
    ): Flow<Result<AddJournalResponse>> = flow {
        try {
            val response = apiService.addJournal(date, emotion, note)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateApiService(apiService: ApiService) {
        this.apiService = apiService
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference,apiService)
            }.also { instance = it }
    }
}