package com.dicoding.picodiploma.pan.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class AddJournalResponse(

	@field:SerializedName("date")
	val date: Date,

	@field:SerializedName("note")
	val note: String,

	@field:SerializedName("emotion")
	val emotion: String
)
