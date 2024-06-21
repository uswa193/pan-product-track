package com.capstone.pan.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.capstone.pan.data.UserRepository
import com.capstone.pan.data.retrofit.response.ProfileEditResponse
import com.capstone.pan.di.Result
import kotlinx.coroutines.flow.first

class ProfileEditViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

//    fun getSession() = userRepository.getProfile().asLiveData()

    fun updateUserProfile(username: String, email: String, password: String): LiveData<Result<ProfileEditResponse>> {
        return liveData {
            emit(Result.Loading)
            val result = userRepository.updateUserProfile(username, email, password).first()
            emit(result)
        }
    }
}
