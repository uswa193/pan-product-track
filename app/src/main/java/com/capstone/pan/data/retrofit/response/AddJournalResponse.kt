package com.capstone.pan.data.retrofit.response

import java.util.Date
import com.google.gson.annotations.SerializedName

data class AddJournalResponse(

    @field:SerializedName("date")
	val date: Date,

    @field:SerializedName("note")
	val note: String,

    @field:SerializedName("emotion")
	val emotion: String,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("error")
	val error: Boolean,


    )
