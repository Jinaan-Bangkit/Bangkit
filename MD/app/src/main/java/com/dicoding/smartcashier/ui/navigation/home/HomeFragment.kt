package com.dicoding.smartcashier.ui.navigation.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.smartcashier.R
import com.dicoding.smartcashier.data.pref.UserPreference
import com.dicoding.smartcashier.data.remote.response.ItemsResponse
import com.dicoding.smartcashier.databinding.FragmentHomeBinding
import com.dicoding.smartcashier.ui.ViewModelFactory
import com.dicoding.smartcashier.ui.login.LoginActivity

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    private lateinit var adapter: ItemAdapter
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
    }
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        binding.rvItem.layoutManager = layoutManager
        adapter = ItemAdapter()
        binding.rvItem.adapter = adapter

        homeViewModel.getItem()
        homeViewModel.getReport()

       // setupView()
        setupViewModel()
        setupSearchView()

        return binding.root
    }
    private fun setupSearchView() {
        searchView = binding.search
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    homeViewModel.getItem(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    homeViewModel.getItem()
                }
                return false
            }
        })
    }
    private fun setupViewModel() {
        homeViewModel.listItem.observe(viewLifecycleOwner) {
            setRecycler(it)
        }
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        homeViewModel.reportResponse.observe(viewLifecycleOwner) {report->
            report?.let {
                binding.tvPriceProfit.text = it.totalKeuntungan
                binding.tvPriceIncome.text = it.totalPenjualan.toString()
                binding.tvPriceAmount.text = it.jumlahTransaksi.toString()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                Intent(requireContext(), LoginActivity::class.java).also {
                    startActivity(it)
                    Toast.makeText(requireContext(), getString(R.string.logout), Toast.LENGTH_SHORT).show()
                    homeViewModel.logout()
                    requireActivity().finish()
                    return true
                }
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

//    private fun setupView() {
//        homeViewModel.getItem()
//    }

    private fun setRecycler(list: List<ItemsResponse>) {
        adapter.submitList(list)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
