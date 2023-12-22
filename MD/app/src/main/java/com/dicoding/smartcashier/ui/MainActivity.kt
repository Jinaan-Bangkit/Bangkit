package com.dicoding.smartcashier.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.dicoding.smartcashier.R
import com.dicoding.smartcashier.data.pref.UserPreference
import com.dicoding.smartcashier.databinding.ActivityMainBinding
import com.dicoding.smartcashier.ui.login.LoginActivity
import com.dicoding.smartcashier.ui.navigation.home.dataStore
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[MainViewModel::class.java]
        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navController = navHostFragment.navController
        val navBottom: BottomNavigationView = findViewById(R.id.bottom_navigation)

        setupWithNavController(navBottom, navController)
//                val navController = findNavController(R.id.nav_fragment)
//        val appBarConfiguration = AppBarConfiguration.Builder(
//            R.id.nav_home,
//            R.id.nav_add,
//            R.id.nav_history,
//            R.id.nav_transaction
//        ).build()

        // Menambahkan baris berikut untuk mengatur ActionBar
        //setupActionBarWithNavController(navController, appBarConfiguration)

        //navBottom.setupWithNavController(navController)

        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}
