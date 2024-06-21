package com.capstone.pan.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.pan.di.Result
import com.capstone.pan.view.ViewModelFactory
import com.dicoding.picodiploma.pan.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileEditFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileEditViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load user session data and populate the fields
//        viewModel.getSession().observe(viewLifecycleOwner) { user ->
//            binding.nameEditText.setText(user.name)
//            binding.emailEditText.setText(user.email)
//        }

        binding.saveButton.setOnClickListener {
            val username = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            // Check if any field is empty
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showToast("Harap masukkan username, email, dan password")
                return@setOnClickListener
            }

            // Update user profile
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.updateUserProfile(username, email, password).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Success -> {
                            showToast(result.data.message)
                        }
                        is Result.Error -> {
                            showToast(result.error)
                        }

                        is Result.Loading -> {
                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
