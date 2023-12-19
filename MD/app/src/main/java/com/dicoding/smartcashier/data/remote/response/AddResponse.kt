package com.dicoding.smartcashier.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddResponse(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("hargaJual")
	val hargaJual: String? = null,

	@field:SerializedName("hargaBeli")
	val hargaBeli: String? = null,

	@field:SerializedName("stock")
	val stock: String? = null
)
