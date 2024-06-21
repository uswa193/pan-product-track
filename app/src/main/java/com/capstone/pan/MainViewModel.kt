package com.capstone.pan

import androidx.lifecycle.ViewModel
import com.capstone.pan.data.UserRepository
import com.capstone.pan.data.pref.UserModel
import com.capstone.pan.data.pref.UserModelRegist
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): Flow<UserModel> {
        return repository.getSession()
    }




}