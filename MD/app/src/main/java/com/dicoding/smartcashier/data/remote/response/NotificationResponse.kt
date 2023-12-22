package com.dicoding.smartcashier.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class NotificationResponse(

	@field:SerializedName("NotificationResponse")
	val notificationResponse: List<NotificationResponseItem?>? = null
)
@Parcelize
data class NotificationResponseItem(

	@field:SerializedName("keterangan")
	val keterangan: String,

	@field:SerializedName("nama_barang")
	val namaBarang: String
): Parcelable
