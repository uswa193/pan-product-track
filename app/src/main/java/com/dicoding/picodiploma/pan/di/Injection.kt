package com.dicoding.picodiploma.pan.di

import android.content.Context
import com.dicoding.picodiploma.pan.data.UserRepository
import com.dicoding.picodiploma.pan.data.pref.UserPreference
import com.dicoding.picodiploma.pan.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}