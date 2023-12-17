package com.dicoding.smartcashier.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.smartcashier.R
import com.dicoding.smartcashier.data.pref.UserPreference
import com.dicoding.smartcashier.databinding.ActivityMainBinding
import com.dicoding.smartcashier.ui.login.LoginActivity
import com.dicoding.smartcashier.ui.navigation.home.dataStore
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        mainViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[MainViewModel::class.java]
        mainViewModel.getUser().observe(this) {user->
            if (user.isLogin) {
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        val navBottom: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navController = findNavController(R.id.nav_fragment)
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home,
            R.id.nav_add,
            R.id.nav_history,
            R.id.nav_transaction
        ).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navBottom.setupWithNavController(navController)
//        replaceFragment(HomeFragment())
//
//        mainViewModel.getUser().observe(this) {user->
//            if (user.token.isNotEmpty()) {
//                replaceFragment(HomeFragment())
//            } else {
//                startActivity(Intent(this, LoginActivity::class.java))
//                finish()
//            }
//        }
//
//        binding.bottomNavigation.setOnItemSelectedListener { menuItem->
//            when(menuItem.itemId) {
//                R.id.nav_home -> replaceFragment(HomeFragment())
//                R.id.nav_add -> replaceFragment(AddFragment())
//                R.id.nav_history -> replaceFragment(HistoryFragment())
//                R.id.nav_transaction -> replaceFragment(TransactionFragment())
//            }
//            true
//        }
//
        mainViewModel.getUser().observe(this) {user->
            if (user.isLogin) {
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

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

//    private fun replaceFragment(fragment: Fragment) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.frameLayout, fragment)
//        fragmentTransaction.commit()
//
//    }

}