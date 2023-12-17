package com.dicoding.smartcashier.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("password")
	val password: String?,

	@field:SerializedName("name")
	val name: String?
)
