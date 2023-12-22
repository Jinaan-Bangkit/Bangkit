package com.dicoding.smartcashier.data.remote.response

import com.google.gson.annotations.SerializedName

data class ReportResponse(

	@field:SerializedName("jumlah_transaksi")
	val jumlahTransaksi: String,

	@field:SerializedName("total_keuntungan")
	val totalKeuntungan: String,

	@field:SerializedName("total_penjualan")
	val totalPenjualan: String
)
