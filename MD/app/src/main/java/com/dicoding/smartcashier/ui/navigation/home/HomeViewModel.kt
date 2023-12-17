package com.dicoding.smartcashier.ui.navigation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.smartcashier.data.remote.api.ApiConfig
import com.dicoding.smartcashier.data.remote.response.ItemsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listItem = MutableLiveData<List<ItemsResponse>>()
    val listItem: LiveData<List<ItemsResponse>> = _listItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getItem() {
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
                    }
                }
            }

            override fun onFailure(call: Call<List<ItemsResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: Failed", t)
            }
        })
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}
