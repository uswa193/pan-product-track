package com.capstone.pan.view.journal.create

import androidx.lifecycle.ViewModel
import com.capstone.pan.data.UserRepository
import com.capstone.pan.data.retrofit.response.AddJournalResponse
import com.capstone.pan.di.Journal
import com.capstone.pan.di.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class JournalViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun saveJournal(journal: Journal): Flow<Result<AddJournalResponse>> {
        return flow {
            userRepository.addJournal(journal)
                .onStart { emit(Result.Loading) } // Emit loading state when the flow starts
                .catch { e -> emit(Result.Error(e.message ?: "An error occurred")) } // Handle exceptions within the flow
                .collect { result -> emit(result) } // Emit the collected result
        }
    }
}
