package com.dicoding.smartcashier.ui.navigation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.smartcashier.data.pref.UserPreference
import com.dicoding.smartcashier.data.remote.api.ApiConfig
import com.dicoding.smartcashier.data.remote.response.ItemsResponse
import com.dicoding.smartcashier.data.remote.response.ReportResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val pref: UserPreference) : ViewModel() {
    private val _listItem = MutableLiveData<List<ItemsResponse>>()
    val listItem: LiveData<List<ItemsResponse>> = _listItem

    private val _reportResponse = MutableLiveData<ReportResponse>()
    val reportResponse: LiveData<ReportResponse> = _reportResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getItem(query: String? = null) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllItems()
        client.enqueue(object : Callback<List<ItemsResponse>> {
            override fun onResponse(
                call: Call<List<ItemsResponse>>,
                response: Response<List<ItemsResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        _listItem.value = it
                        val filteredList = if (query.isNullOrBlank()) {
                            it
                        } else {
                            it.filter { item ->
                                item.nama?.contains(query, true) == true
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<ItemsResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: Failed", t)
            }
        })
    }

    fun getReport() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getReport()
        client.enqueue(object : Callback<ReportResponse> {
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _reportResponse.value = response.body()
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: Failed", t)
            }

        })

    }
    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}
