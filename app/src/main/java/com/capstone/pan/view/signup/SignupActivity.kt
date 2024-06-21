package com.capstone.pan.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.pan.R
import com.dicoding.picodiploma.pan.databinding.ActivitySignupBinding
import com.capstone.pan.di.Result
import com.capstone.pan.view.ViewModelFactory
import com.capstone.pan.view.login.LoginActivity
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

    }

    private fun setupView() {
        // Pengaturan tampilan
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        // Aksi pada tombol login
        binding.loginTextButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Aksi pada tombol signup
        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val username = binding.nameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            var isValid = true



            if (username.isEmpty()) {
                binding.nameEditText.error = getString(R.string.error_empty_field)
                isValid = false
            }
            if (email.isEmpty()) {
                binding.emailEditText.error = getString(R.string.error_empty_field)
                isValid = false
            }
            if (password.isEmpty()) {
                binding.passwordEditText.error = getString(R.string.error_empty_field)
                isValid = false
            }

            if (isValid) {
                lifecycleScope.launch {
                    viewModel.userRegister(username, email, password).collect { result ->
                        when (result) {
                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showToast(result.data.message)

                                showLoading(false)
                                showSuccessDialog()
                            }

                            is Result.Error -> {
                                showToast(result.error)
                                showLoading(false)
                            }
                        }
                    }
                }
            }
        }
    }

        private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Success")
            setMessage("Registration successful!")
            setPositiveButton("Continue") { _, _ -> finish() }
            create()
            show()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val animDuration = 100L
        AnimatorSet().apply {
            playSequentially(
                ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(animDuration),
                ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(animDuration),
                ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(animDuration),
                ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(animDuration),
                ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(animDuration),
                ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(animDuration),
                ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(animDuration),
                ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(animDuration)
            )
            startDelay = 100
        }.start()
    }
}
