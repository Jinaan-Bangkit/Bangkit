package com.dicoding.smartcashier.ui.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.smartcashier.data.pref.UserModel
import com.dicoding.smartcashier.data.pref.UserPreference
import com.dicoding.smartcashier.databinding.ActivityLoginBinding
import com.dicoding.smartcashier.ui.ViewModelFactory
import com.dicoding.smartcashier.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private val Context.dataStore by preferencesDataStore("settings")

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
        setupAction()
        registerAction()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[LoginViewModel::class.java]
        loginViewModel.getUser().observe(this, { user->
            this.user = user
        })
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
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text
            val password = binding.edtPassword.text

            when {
                email?.isEmpty()!! -> {
                    binding.emailEditTextLayout.error = "Input your Email"
                }
                password?.isEmpty()!! -> {
                    binding.passwordEditTextLayout.error = "Input your Password"
                }

            }
        }
    }

//    private fun setLogin() {
//        binding.apply {
//            val email = edtEmail.text.toString()
//            val password = edtPassword.text.toString()
//            loginViewModel.getLogin(email, password)
//        }
//    }

    private fun registerAction() {
        binding.apply {
            tvRegister.setOnClickListener {
                Intent(this@LoginActivity, RegisterActivity::class.java).also { register->
                    startActivity(register)
                    finishAffinity()
                }
            }
        }
    }
}