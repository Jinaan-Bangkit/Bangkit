package com.dicoding.smartcashier.data.remote.response

import com.google.gson.annotations.SerializedName

data class ItemsResponse(

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("id_barang")
	val idBarang: String? = null,

	@field:SerializedName("harga_beli")
	val hargaBeli: Int? = null,

	@field:SerializedName("harga_jual")
	val hargaJual: Int? = null,

	@field:SerializedName("stock")
	val stock: Int? = null
)
