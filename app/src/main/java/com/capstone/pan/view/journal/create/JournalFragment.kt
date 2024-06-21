package com.capstone.pan.view.journal.create

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.pan.R
import com.dicoding.picodiploma.pan.databinding.FragmentJournalBinding
import com.capstone.pan.di.Journal
import com.capstone.pan.di.Result
import com.capstone.pan.view.ViewModelFactory
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale

class JournalFragment : Fragment() {

    private var _binding: FragmentJournalBinding? = null
    private val binding get() = _binding!!

    private val journalViewModel by viewModels<JournalViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var selectedDate: Date? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJournalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textView6.setOnClickListener {
            showDatePickerDialog()
        }

        val emotionButtons = listOf(
            binding.button to "Happy",
            binding.button2 to "Sad",
            binding.button3 to "Fear",
            binding.button4 to "Love",
            binding.button5 to "Angry"
        )

        emotionButtons.forEach { (button, emotion) ->
            button.setBackgroundColor(resources.getColor(R.color.white, null))
            button.setOnClickListener {
                emotionButtons.forEach { (b, _) ->
                    b.setBackgroundColor(resources.getColor(R.color.white, null))
                }
                button.setBackgroundColor(resources.getColor(getEmotionColor(emotion), null))
            }
        }

        binding.button6.setOnClickListener {
            val note = binding.editText.text.toString()
            val emotion = getSelectedEmotion()

            if (selectedDate != null && emotion.isNotEmpty() && note.isNotEmpty()) {
                val journal = Journal(selectedDate!!, emotion, note)

                lifecycleScope.launch {
                    journalViewModel.saveJournal(journal).collect { result ->
                        when (result) {
                            is Result.Success -> {
                                val responseMessage = result.data.message
                                showToast(responseMessage)
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
            } else {
                // Display an error message if any field is empty
                Toast.makeText(requireContext(), "Silahkan periksa semua field", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val cal = Calendar.getInstance()
                cal.set(selectedYear, selectedMonth, selectedDay)
                selectedDate = cal.time
                val selectedDateString = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate)
                binding.textView6.text = selectedDateString
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun getEmotionColor(emotion: String): Int {
        return when (emotion) {
            "Happy" -> R.color.yellow
            "Sad" -> R.color.purple_200
            "Fear" -> R.color.green
            "Love" -> R.color.pink
            "Angry" -> R.color.red
            else -> R.color.white
        }
    }

    private fun getSelectedEmotion(): String {
        val emotionButtons = listOf(
            binding.button to "Happy",
            binding.button2 to "Sad",
            binding.button3 to "Fear",
            binding.button4 to "Love",
            binding.button5 to "Angry"
        )

        emotionButtons.forEach { (button, emotion) ->
            if (button.backgroundTintList?.defaultColor != resources.getColor(R.color.white, null)) {
                return emotion
            }
        }
        return ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
