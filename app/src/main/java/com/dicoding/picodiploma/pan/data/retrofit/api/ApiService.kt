package com.dicoding.picodiploma.pan.data.retrofit.api



import com.dicoding.picodiploma.pan.data.retrofit.response.AddJournalResponse
import com.dicoding.picodiploma.pan.data.retrofit.response.LoginResponse
import com.dicoding.picodiploma.pan.data.retrofit.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.POST
import java.util.Date


interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse


    @FormUrlEncoded
    @POST("register")
    suspend fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse


    @FormUrlEncoded
    @POST("add")
    suspend fun addJournal(
        @Field("date") date: Date,
        @Field("emotion") emotion: String,
        @Field("note") note: String
    ): AddJournalResponse


    @FormUrlEncoded
    @POST("add")
    suspend fun seeJournal(
        @Field("date") date: Date,
        @Field("emotion") emotion: String,
        @Field("note") note: String
    ): AddJournalResponse
}