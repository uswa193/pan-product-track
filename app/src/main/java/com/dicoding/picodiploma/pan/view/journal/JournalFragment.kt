package com.dicoding.picodiploma.pan.view.journal

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.pan.R
import com.dicoding.picodiploma.pan.databinding.FragmentJournalBinding
import java.util.Calendar

class JournalFragment : Fragment() {

    private var _binding: FragmentJournalBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val journalViewModel =
            ViewModelProvider(this).get(JournalViewModel::class.java)

        _binding = FragmentJournalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttons = listOf(
            binding.button to R.color.yellow,
            binding.button2 to R.color.purple_200,
            binding.button3 to R.color.green
        )

        buttons.forEach { (button, color) ->
            button.setBackgroundColor(resources.getColor(R.color.white, null))
            button.setOnClickListener {
                button.setBackgroundColor(resources.getColor(color, null))
            }
        }

        binding.textView6.setOnClickListener {
            showDatePickerDialog()
        }

        journalViewModel.text.observe(viewLifecycleOwner) {
            // Observing text from ViewModel
        }
        return root
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Handle the selected date here
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.textView6.text = selectedDate // Update the button text with the selected date
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
