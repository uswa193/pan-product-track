package com.capstone.pan.view.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.pan.R
import com.dicoding.picodiploma.pan.databinding.FragmentDashboardBinding
import com.capstone.pan.view.ViewModelFactory
import com.capstone.pan.view.condition.ConditionActivity
import com.capstone.pan.view.chat.ChatFragment
import com.capstone.pan.view.journal.list.ListJournalActivity

@Suppress("DEPRECATION")
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<DashboardViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observe userName StateFlow to update UI when user session data is fetched
        lifecycleScope.launchWhenStarted {
            viewModel.userName.collect { userName ->
                binding.name.text = userName
            }
        }

        binding.btnLogout.setOnClickListener{
            viewModel.logout()
        }
        binding.chatButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_pan_main, ChatFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.emotionCheck.setOnClickListener {
            val intent = Intent(activity, ConditionActivity::class.java)
            startActivity(intent)
        }

        binding.journalCheck.setOnClickListener {
            val intent = Intent(activity, ListJournalActivity::class.java)
            startActivity(intent)
        }

        // Fetch user session data
        viewModel.getUserSession()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
