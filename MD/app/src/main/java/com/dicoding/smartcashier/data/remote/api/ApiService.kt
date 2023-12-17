package com.dicoding.smartcashier.data.remote.api

import com.dicoding.smartcashier.data.remote.response.ItemsResponse
import com.dicoding.smartcashier.data.remote.response.LoginResponse
import com.dicoding.smartcashier.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("name") name: String,
        @Field("password")password: String
    ): Call<LoginResponse>
    @GET("barang")
    fun getAllItems(): Call<List<ItemsResponse>>


}