package com.capstone.pan.data

import com.capstone.pan.data.pref.UserModel
import com.capstone.pan.data.pref.UserModelRegist
import com.capstone.pan.data.pref.UserPreference
import com.capstone.pan.data.retrofit.api.ApiService
import com.capstone.pan.data.retrofit.response.AddJournalResponse
import com.capstone.pan.data.retrofit.response.EmotionlResponse
import com.capstone.pan.data.retrofit.response.LoginResponse
import com.capstone.pan.data.retrofit.response.ProfileEditResponse
import com.capstone.pan.data.retrofit.response.RegisterResponse
import com.capstone.pan.data.retrofit.response.SeeJournalResponse
import com.capstone.pan.di.Journal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.capstone.pan.di.Result
import kotlinx.coroutines.flow.flowOn

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private var apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getProfile(): Flow<UserModelRegist> {
        return userPreference.getProfile()
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
        username: String,
        email: String,
        password: String
    ): Flow<Result<RegisterResponse>> = flow {
        try {
            val response = apiService.userRegister(username, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun addJournal(
        journal: Journal
    ): Flow<Result<AddJournalResponse>> = flow {
        try {
            val response = apiService.addJournal(journal)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    fun getJournalsByUserId(userId: String): Flow<Result<SeeJournalResponse>> = flow {
        try {
            val response = apiService.getJournalsByUserId(userId)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun classifyEmotion(): Flow<Result<EmotionlResponse>> = flow {
        try {
            val response = apiService.classifyEmotion()
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error("Failed to classify emotion: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateUserProfile(username: String, email: String, password: String): Flow<Result<ProfileEditResponse>> = flow {
        try {
            val response = apiService.editProfile(username, email, password)
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
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
