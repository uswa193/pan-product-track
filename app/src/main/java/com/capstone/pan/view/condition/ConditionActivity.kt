package com.capstone.pan.view.condition

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.pan.view.ViewModelFactory
import com.capstone.pan.view.journal.list.ListJournalViewModel
import com.dicoding.picodiploma.pan.R
import com.dicoding.picodiploma.pan.databinding.ActivityConditionBinding

class ConditionActivity : AppCompatActivity() {
    private var _binding: ActivityConditionBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ConditionViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_condition)


        observeEmotion()
        viewModel.fetchEmotionData()
    }

    @SuppressLint("SetTextI18n")
    private fun observeEmotion() {
        viewModel.emotion.observe(this) { emotion ->
            val emojiResId = when (emotion) {
                "cinta" -> R.drawable.object_cinta
                "marah" -> R.drawable.object_marah
                "sedih" -> R.drawable.object_sedih
                "senang" -> R.drawable.object_senang
                "takut" -> R.drawable.object_takut
                else -> R.drawable.edit_text_shadow // Default emoji for unknown emotion
            }

            binding.condition.text = emotion
            binding.emoticon.setImageResource(emojiResId)
            binding.yourCondition.text = "Saya merasa $emotion"
        }
    }
}
