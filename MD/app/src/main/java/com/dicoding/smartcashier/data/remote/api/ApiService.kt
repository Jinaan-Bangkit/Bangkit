package com.dicoding.smartcashier.data.remote.api

import com.dicoding.smartcashier.data.remote.response.AddResponse
import com.dicoding.smartcashier.data.remote.response.ItemsResponse
import com.dicoding.smartcashier.data.remote.response.LoginResponse
import com.dicoding.smartcashier.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>
    @GET("login")
    fun login(
        @Query("name") name: String,
        @Query("password")password: String
    ): Call<List<LoginResponse>>
    @GET("barang")
    fun getAllItems(): Call<List<ItemsResponse>>

    @POST("barang")
    fun addItems(): Call<AddResponse>


}