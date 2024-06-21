package com.capstone.pan.view.journal.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.pan.data.retrofit.response.JournalsItem

class DetailJournalViewModel : ViewModel() {

    private val _journal = MutableLiveData<JournalsItem>()
    val journal: LiveData<JournalsItem>
        get() = _journal

    fun setJournal(journalItem: JournalsItem) {
        _journal.value = journalItem
    }
}
