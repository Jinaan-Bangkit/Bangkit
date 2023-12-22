package com.dicoding.smartcashier.ui.navigation.addtransaction

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.smartcashier.data.remote.api.ApiConfig
import com.dicoding.smartcashier.data.remote.response.NotificationResponseItem
import com.dicoding.smartcashier.data.remote.response.RestockResponse
import com.dicoding.smartcashier.ui.navigation.notification.NotificationViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTransactionViewModel : ViewModel(){

    val input = MutableLiveData<RestockResponse>()
    fun addTransaction(idBarang: String, jumlah: String) {
        val client = ApiConfig.getApiService().addTransaction(idBarang, jumlah)
        client.enqueue(object : Callback<RestockResponse> {
            override fun onResponse(
                call: Call<RestockResponse>,
                response: Response<RestockResponse>
            ) {
                if (response.isSuccessful) {
                    input.postValue(response.body())
                    getNotification(idBarang, jumlah)
                }
            }

            override fun onFailure(call: Call<RestockResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getNotification(namaBarang: String, jumlah: String) {
        val viewModel = ViewModelProvider.NewInstanceFactory().create(NotificationViewModel::class.java)
        val client = ApiConfig.getApiService().getNotification(namaBarang, jumlah)
        client.enqueue(object : Callback<List<NotificationResponseItem>> {
            override fun onResponse(
                call: Call<List<NotificationResponseItem>>,
                response: Response<List<NotificationResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        viewModel.updateList(it)
                        viewModel.getNotifClick(namaBarang, jumlah)
                    }
                }
            }

            override fun onFailure(call: Call<List<NotificationResponseItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: Failed", t)
            }

        })
    }

    companion object {
        private const val TAG = "AddTransactionViewModel"
    }
}