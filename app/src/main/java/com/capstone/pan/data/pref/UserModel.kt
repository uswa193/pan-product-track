package com.capstone.pan.data.pref

data class UserModel(
    val email: String,
    val token: String,
//    val userId: String,
    val isLogin: Boolean = false
)