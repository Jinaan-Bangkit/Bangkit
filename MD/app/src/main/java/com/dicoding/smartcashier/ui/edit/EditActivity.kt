package com.dicoding.smartcashier.ui.edit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.smartcashier.databinding.ActivityEditBinding
import com.dicoding.smartcashier.ui.navigation.home.ItemAdapter

class EditActivity : AppCompatActivity() {

    private lateinit var itemAdapter: ItemAdapter
    private lateinit var binding : ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = ItemAdapter

    }
}