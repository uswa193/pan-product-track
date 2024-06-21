package com.capstone.pan.view.journal.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.pan.data.retrofit.response.JournalsItem
import com.dicoding.picodiploma.pan.databinding.ActivityDetailJournalBinding

@Suppress("DEPRECATION")
class DetailJournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailJournalBinding
    private lateinit var viewModel: DetailJournalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[DetailJournalViewModel::class.java]

        // Get journal item from intent
        val journalItem = intent.getSerializableExtra("journalItem") as? JournalsItem
        journalItem?.let {
            viewModel.setJournal(it)
        }

        // Observe data from ViewModel
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.journal.observe(this) { journal ->
            journal?.let {
                showJournalDetails(it)
            }
        }
    }

    private fun showJournalDetails(journal: JournalsItem) {
        binding.textViewDate.text = journal.date?.toString() ?: ""
        binding.textViewEmotion.text = journal.emotion ?: ""
        binding.textViewNote.text = journal.note ?: ""
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
