package com.dicoding.smartcashier.ui.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.smartcashier.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}