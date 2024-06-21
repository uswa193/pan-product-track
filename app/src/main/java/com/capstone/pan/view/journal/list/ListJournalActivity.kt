package com.capstone.pan.view.journal.list
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.pan.databinding.ActivityListJournalBinding
import com.capstone.pan.di.Result
import com.capstone.pan.view.ViewModelFactory
import com.capstone.pan.view.journal.detail.DetailJournalActivity

class ListJournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListJournalBinding
    private lateinit var journalAdapter: JournalAdapter
    private val viewModel by viewModels<ListJournalViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup RecyclerView
        setupRecyclerView()

        // Observe data from ViewModel
        observeViewModel()

        // Example call to fetch journal list
        val userId = "BaK368tzPzR9eTvFcoUp"
        viewModel.getJournalsByUserId(userId)
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        journalAdapter = JournalAdapter { journal ->
            val intent = Intent(this, DetailJournalActivity::class.java)
            intent.putExtra("journalItem", journal)
            startActivity(intent)
        }
        recyclerView.adapter = journalAdapter
    }

    private fun observeViewModel() {
        viewModel.journalList.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val journals = result.data.journals ?: emptyList()
                    journalAdapter.submitList(journals)
                    showLoading(false)
                }
                is Result.Error -> {
                    showToast(result.error)
                    showLoading(false)
                }
                is Result.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
