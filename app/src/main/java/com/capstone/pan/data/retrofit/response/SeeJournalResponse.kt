package com.capstone.pan.data.retrofit.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SeeJournalResponse(

	@field:SerializedName("journals")
	val journals: List<JournalsItem?>? = null,
	@field:SerializedName("message")
	val message: String,

)

data class CreatedAt(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
): Serializable

data class Date(

	@field:SerializedName("_nanoseconds")
	val nanoseconds: Int? = null,

	@field:SerializedName("_seconds")
	val seconds: Int? = null
)

data class JournalsItem(

	@field:SerializedName("date")
	val date:  String? = null,

	@field:SerializedName("note")
	val note: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: CreatedAt? = null,

	@field:SerializedName("emotion")
	val emotion: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null
): Serializable
