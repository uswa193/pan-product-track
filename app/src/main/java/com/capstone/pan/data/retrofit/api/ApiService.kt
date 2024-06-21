package com.capstone.pan.data.retrofit.api



import com.capstone.pan.data.retrofit.response.AddJournalResponse
import com.capstone.pan.data.retrofit.response.EmotionlResponse
import com.capstone.pan.data.retrofit.response.LoginResponse
import com.capstone.pan.data.retrofit.response.ProfileEditResponse
import com.capstone.pan.data.retrofit.response.RegisterResponse
import com.capstone.pan.data.retrofit.response.SeeJournalResponse
import com.capstone.pan.di.Journal
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse


    @FormUrlEncoded
    @POST("signup")
    suspend fun userRegister(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse




    
    @POST("journal/add")
    suspend fun addJournal(
        @Body journal: Journal
    ): AddJournalResponse

    @GET("journal/{userId}")
    suspend fun getJournalsByUserId(
        @Path("userId") userId: String
    ): SeeJournalResponse

    @GET("classify_emotion")
    suspend fun classifyEmotion(): EmotionlResponse

    @FormUrlEncoded
    @POST("profile/edit")
    suspend fun editProfile(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ProfileEditResponse
}