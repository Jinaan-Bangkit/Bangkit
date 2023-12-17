package com.dicoding.smartcashier.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.smartcashier.R
import com.dicoding.smartcashier.databinding.ActivityRegisterBinding
import com.dicoding.smartcashier.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RegisterViewModel::class.java]
        registerViewModel.getSignup().observe(this) {register ->
            if (register == null) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
        userRegister()
        setLoginAction()
    }

    private fun userRegister() {
        val username = binding.edtUsername.text
        val email = binding.edtEmail.text
        val password = binding.edtPassword.text

        binding.btnRegister.setOnClickListener {
            when {
                username?.isEmpty()!! -> {
                    Toast.makeText(this, getString(R.string.empty_username), Toast.LENGTH_SHORT).show()
                } email?.isEmpty()!! -> {
                    Toast.makeText(this, getString(R.string.empty_email), Toast.LENGTH_SHORT).show()
                } password?.isEmpty()!! -> {
                    Toast.makeText(this, getString(R.string.empty_password), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    setupRegister()
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupRegister() {
        binding.apply {
            val name = edtUsername.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            registerViewModel.register(name, email, password)
        }
    }

    private fun setLoginAction() {
        binding.apply {
            buttonLogin.setOnClickListener {
                Intent(this@RegisterActivity, LoginActivity::class.java).also { login ->
                    startActivity(login)
                    finishAffinity()
                }
            }
        }
    }
}