package com.dicoding.smartcashier.ui.navigation.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.smartcashier.data.remote.api.ApiConfig
import com.dicoding.smartcashier.data.remote.response.NotificationResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationViewModel: ViewModel() {

    private val _listNotif = MutableLiveData<List<NotificationResponseItem>>()
    val listNotif: LiveData<List<NotificationResponseItem>> = _listNotif

    fun updateList(newList: List<NotificationResponseItem>) {
        _listNotif.value = newList
    }

//    fun getNotification(namaBarang: String, jumlah: String) {
//        val client = ApiConfig.getApiService().getNotification(namaBarang, jumlah)
//        client.enqueue(object : Callback<List<NotificationResponseItem>> {
//            override fun onResponse(
//                call: Call<List<NotificationResponseItem>>,
//                response: Response<List<NotificationResponseItem>>
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    responseBody?.let {
//                        _listNotif.value = it
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<NotificationResponseItem>>, t: Throwable) {
//                Log.e(TAG, "onFailure: Failed", t)
//            }
//
//        })
//    }
    fun getNotifClick(namaBarang: String, keterangan: String) {
        val client = ApiConfig.getApiService().getNotification(namaBarang, keterangan)
        client.enqueue(object : Callback<List<NotificationResponseItem>> {
            override fun onResponse(
                call: Call<List<NotificationResponseItem>>,
                response: Response<List<NotificationResponseItem>>
            ) {
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        _listNotif.value = it
                        //setClickedNotification(it[0])
                    }
                }
            }

            override fun onFailure(call: Call<List<NotificationResponseItem>>, t: Throwable) {
                Log.e(TAG, "onFailure: Failed", t)
            }

        })
    }

    companion object {
        private val TAG = "NotificationViewModel"
    }
}