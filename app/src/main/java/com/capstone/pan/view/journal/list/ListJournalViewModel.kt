package com.capstone.pan.view.journal.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.pan.data.UserRepository
import com.capstone.pan.data.retrofit.response.SeeJournalResponse
import com.capstone.pan.di.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListJournalViewModel(private val userRepository: UserRepository) : ViewModel() {


//    suspend fun getJournalsByUserId(userId: String) = userRepository.getJournalsByUserId(userId)

    private val _journalList = MutableLiveData<Result<SeeJournalResponse>>()
    val journalList: LiveData<Result<SeeJournalResponse>>
        get() = _journalList

    fun getJournalsByUserId(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getJournalsByUserId(userId).collect { result ->
                _journalList.postValue(result)

            }
        }
    }
}
