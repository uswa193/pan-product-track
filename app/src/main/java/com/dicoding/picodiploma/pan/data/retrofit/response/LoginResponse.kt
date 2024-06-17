package com.dicoding.picodiploma.pan.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("userId")
	val userId: String
)
