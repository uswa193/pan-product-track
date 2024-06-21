package com.capstone.pan.di

import android.content.Context
import com.capstone.pan.data.UserRepository
import com.capstone.pan.data.pref.UserPreference
import com.capstone.pan.data.retrofit.api.ApiConfig
import com.capstone.pan.data.retrofit.api.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val pref = provideUserPreference(context)

        val user = runBlocking { pref.getSession().first() }

        val apiService = provideApiService(user.token)

        return UserRepository.getInstance(pref, apiService)
    }

    fun provideApiService(token: String): ApiService {
        return ApiConfig.getApiService(token)
    }

    fun provideUserPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context)
    }
}
