package com.dicoding.smartcashier.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.smartcashier.data.pref.UserModel
import com.dicoding.smartcashier.data.pref.UserPreference

class MainViewModel(private val preference: UserPreference): ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return preference.getUser().asLiveData()
    }

}