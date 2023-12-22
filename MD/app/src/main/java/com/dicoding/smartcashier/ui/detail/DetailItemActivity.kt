package com.dicoding.smartcashier.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.dicoding.smartcashier.databinding.ActivityDetailItemBinding

class DetailItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        binding.apply {
            tvId.text = intent.getStringExtra(ID)
            tvName.text = intent.getStringExtra(NAME)
            tvPurchase.text = intent.getIntExtra(PURCHASE, 0).toString()
            tvSale.text = intent.getIntExtra(SALE, 0).toString()
            tvStock.text = intent.getIntExtra(STOCK, 0).toString()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val PURCHASE = "purchase"
        const val SALE = "sale"
        const val STOCK ="stock"
    }
}