package com.dicoding.smartcashier.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.smartcashier.data.pref.UserModel
import com.dicoding.smartcashier.data.pref.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference): ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun login(user: UserModel) {
        viewModelScope.launch {
            pref.login(user)
        }
    }
}