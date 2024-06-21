package com.capstone.pan.view.condition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.pan.data.UserRepository
import com.capstone.pan.data.retrofit.response.EmotionlResponse
import com.capstone.pan.di.Result
import kotlinx.coroutines.launch

class ConditionViewModel(private val repository: UserRepository) : ViewModel() {

    private val _emotion = MutableLiveData<String>()
    val emotion: LiveData<String>
        get() = _emotion

    fun fetchEmotionData() {
        viewModelScope.launch {
            try {
                repository.classifyEmotion().collect { result ->
                    handleEmotionResponse(result)
                }
            } catch (e: Exception) {
                // Handle exceptions here, for example, show error message
                e.printStackTrace()
            }
        }
    }

    private fun handleEmotionResponse(response: Result<EmotionlResponse>) {
        if (response is Result.Success) {
            _emotion.postValue(response.data.emotion ?: "unknown")
        } else if (response is Result.Error) {
            // Handle errors if any
            // For example, display error message
            _emotion.postValue("unknown")
        }
    }
}
