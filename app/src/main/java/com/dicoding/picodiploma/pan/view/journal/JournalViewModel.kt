package com.dicoding.picodiploma.pan.view.journal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.pan.data.UserRepository
import com.dicoding.picodiploma.pan.data.retrofit.response.AddJournalResponse
import com.dicoding.picodiploma.pan.data.retrofit.response.RegisterResponse
import com.dicoding.picodiploma.pan.di.Result
import kotlinx.coroutines.flow.Flow
import java.util.Date

class JournalViewModel(private val repository: UserRepository) : ViewModel() {
    fun addJournal(date: Date, emotion: String, note: String): Flow<Result<AddJournalResponse>> {
        return repository.addJournal(date, emotion, note)
    }
}