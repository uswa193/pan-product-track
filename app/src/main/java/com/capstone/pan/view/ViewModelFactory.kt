package com.capstone.pan.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.pan.MainViewModel
import com.capstone.pan.data.UserRepository
import com.capstone.pan.di.Injection
import com.capstone.pan.view.condition.ConditionViewModel
import com.capstone.pan.view.dashboard.DashboardViewModel
import com.capstone.pan.view.journal.create.JournalViewModel
import com.capstone.pan.view.journal.list.ListJournalViewModel
import com.capstone.pan.view.login.LoginViewModel
import com.capstone.pan.view.profile.ProfileEditViewModel
import com.capstone.pan.view.signup.SignUpViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ViewModelFactory private constructor(
    private val repository: UserRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(repository) as T
            }
            modelClass.isAssignableFrom(JournalViewModel::class.java) -> {
                JournalViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ListJournalViewModel::class.java) -> {
                ListJournalViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileEditViewModel::class.java) -> {
                ProfileEditViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ConditionViewModel::class.java) -> {
                ConditionViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val userPreference = Injection.provideUserPreference(context.applicationContext)

                val user = runBlocking {
                    userPreference.getSession().first()
                }

                Injection.provideApiService(user.token)

                INSTANCE ?: ViewModelFactory(
                    Injection.provideRepository(context.applicationContext),

                ).also {
                    INSTANCE = it
                }
            }
        }
    }
}
