package com.dicoding.smartcashier.ui.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.smartcashier.R
import com.dicoding.smartcashier.data.pref.UserModel
import com.dicoding.smartcashier.data.pref.UserPreference
import com.dicoding.smartcashier.databinding.ActivityLoginBinding
import com.dicoding.smartcashier.ui.MainActivity
import com.dicoding.smartcashier.ui.ViewModelFactory
import com.dicoding.smartcashier.ui.register.RegisterActivity
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private val Context.dataStore by preferencesDataStore("settings")

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore)))[LoginViewModel::class.java]
        loginViewModel.getUser().observe(this) {
            this.user = it
        }

        val edtUsername: TextInputLayout = findViewById(R.id.userEditTextLayout)
        edtUsername.isHintAnimationEnabled = false

        val edtPass: TextInputLayout = findViewById(R.id.passwordEditTextLayout)
        edtPass.isHintAnimationEnabled = false

        setupView()
        setupAction()
        setLogin()
        registerAction()
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
            val username = binding.edtUsername.text
            val password = binding.edtPassword.text

            binding.btnLogin.setOnClickListener {
                when {
                    username?.isEmpty()!! -> {
                        Toast.makeText(this, getString(R.string.empty_username), Toast.LENGTH_SHORT).show()
                    }
                    password?.isEmpty()!! -> {
                        Toast.makeText(this, getString(R.string.empty_password), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        setLogin()
                        showLoading()
                    }
                }
            }
        }
    }

    private fun setLogin() {
        binding.apply {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()
            loginViewModel.login(username, password)
        }

        loginViewModel.loginSuccess.observe(this) {loginSuccess ->
            showLoading()
            if (loginSuccess) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerAction() {
        binding.apply {
            tvRegister.setOnClickListener {
                Intent(this@LoginActivity, RegisterActivity::class.java).also { register->
                    startActivity(register)
                    finishAffinity()
                }
            }
        }
        showLoading()
    }

    private fun showLoading() {
        loginViewModel.isLoading.observe(this) { isLoading->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }
}