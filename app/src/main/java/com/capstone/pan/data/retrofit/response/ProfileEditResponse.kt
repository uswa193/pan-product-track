package com.capstone.pan.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class ProfileEditResponse(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("error")
	val error: String,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
