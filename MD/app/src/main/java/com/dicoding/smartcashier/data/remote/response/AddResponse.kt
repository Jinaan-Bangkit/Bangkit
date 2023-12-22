package com.dicoding.smartcashier.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "add_response_table")
data class AddResponse(

	@field:SerializedName("keterangan")
	val keterangan: String,

	@field:SerializedName("nama")
	val nama: String,
	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("hargaJual")
	val hargaJual: String,

	@field:SerializedName("hargaBeli")
	val hargaBeli: String,

	@field:SerializedName("stock")
	val stock: String
)
