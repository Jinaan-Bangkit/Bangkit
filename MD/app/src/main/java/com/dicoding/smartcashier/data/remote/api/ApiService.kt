package com.dicoding.smartcashier.data.remote.api

import com.dicoding.smartcashier.data.remote.response.AddResponse
import com.dicoding.smartcashier.data.remote.response.ItemsResponse
import com.dicoding.smartcashier.data.remote.response.LoginResponse
import com.dicoding.smartcashier.data.remote.response.NotificationResponseItem
import com.dicoding.smartcashier.data.remote.response.RegisterResponse
import com.dicoding.smartcashier.data.remote.response.ReportResponse
import com.dicoding.smartcashier.data.remote.response.RestockResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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
    fun addItems(
        @Field("id") id: String,
        @Field("nama") nama: String,
        @Field("hargaBeli") hargaBeli: String,
        @Field("hargaJual") hargaJual: String,
        @Field("stock") stock: String
    ): Call<AddResponse>

    @FormUrlEncoded
    @PUT("restock")
    fun addTransaction(
        @Field("id_barang") idBarang: String,
        @Field("jumlah") jumlah: String
    ): Call<RestockResponse>
    @GET("reportBulanan")
    fun getReport(): Call<ReportResponse>

    @GET("notifikasi")
    fun getNotification(
        @Field("nama_barang") namaBarang: String,
        @Field("keterangan") keterangan: String
    ): Call<List<NotificationResponseItem>>

}