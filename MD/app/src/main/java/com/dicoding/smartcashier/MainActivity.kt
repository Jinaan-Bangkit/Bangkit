package com.dicoding.smartcashier

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.smartcashier.databinding.ActivityMainBinding
import com.dicoding.smartcashier.ui.navigation.add.AddFragment
import com.dicoding.smartcashier.ui.navigation.history.HistoryFragment
import com.dicoding.smartcashier.ui.navigation.home.HomeFragment
import com.dicoding.smartcashier.ui.navigation.transaction.TransactionFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener { menuItem->
            when(menuItem.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_add -> replaceFragment(AddFragment())
                R.id.nav_history -> replaceFragment(HistoryFragment())
                R.id.nav_transaction -> replaceFragment(TransactionFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()

    }

}