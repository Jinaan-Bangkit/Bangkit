package com.dicoding.smartcashier.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.smartcashier.data.pref.UserModel
import com.dicoding.smartcashier.data.pref.UserPreference
import com.dicoding.smartcashier.data.remote.api.ApiConfig
import com.dicoding.smartcashier.data.remote.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference): ViewModel() {

    private val data = MutableLiveData<LoginResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val loginError = MutableLiveData<String>()
    val loginSuccess = MutableLiveData<Boolean>()

    fun login(name: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(name, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    data.postValue(response.body())
                    response.body()?.let { loginResponse ->
                        saveLogin(UserModel(loginResponse.password ?: "", true))
                        loginSuccess.postValue(true)
                    }
                } else if (response.code() == 401) {
                    loginError.postValue("\n" + "Login failed not authorized")
                } else {
                    Log.e(TAG, "Login Failed: ${response.message()}")
                    loginError.postValue("Login Failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message!!}")
                loginError.postValue("Login Failed: ${t.message}")
            }
        })
    }


    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun saveLogin(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}