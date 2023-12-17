package com.dicoding.smartcashier.ui.navigation.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.smartcashier.data.pref.UserPreference
import com.dicoding.smartcashier.data.remote.response.ItemsResponse
import com.dicoding.smartcashier.databinding.FragmentHomeBinding
import com.dicoding.smartcashier.ui.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    private lateinit var adapter: ItemAdapter
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory(UserPreference.getInstance(requireContext().dataStore))
    }
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val layoutManager = GridLayoutManager(context, 2)
        binding.rvItem.layoutManager = layoutManager
        adapter = ItemAdapter()
        binding.rvItem.adapter = adapter

        homeViewModel.getItem()

        setupView()
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
                    homeViewModel.getItem()
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
    }

    private fun setupView() {
        homeViewModel.getItem()
    }

    private fun setRecycler(list: List<ItemsResponse>) {
        adapter.submitList(list)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
