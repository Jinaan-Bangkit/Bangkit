package com.dicoding.smartcashier.data.remote.response

import com.google.gson.annotations.SerializedName

data class RestockResponse(

	@field:SerializedName("jumlah")
	val jumlah: Int,

	@field:SerializedName("id_barang")
	val idBarang: String
)
