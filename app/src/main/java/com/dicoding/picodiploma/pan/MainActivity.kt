package com.dicoding.picodiploma.pan

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.picodiploma.pan.databinding.ActivityMainBinding
import com.dicoding.picodiploma.pan.view.ViewModelFactory
import com.dicoding.picodiploma.pan.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_pan_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_journal,
                R.id.navigation_dashboard,
                R.id.navigation_profile,
                R.id.navigation_chat

            )
        )

        lifecycleScope.launch {
            viewModel.getSession().collect { user ->
                if (!user.isLogin) {
                    startActivity(Intent(this@MainActivity, WelcomeActivity::class.java)) // Provide context (this@yourActivityClass)
                    finish()
                }
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
