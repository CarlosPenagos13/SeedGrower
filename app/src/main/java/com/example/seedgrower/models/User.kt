package com.example.seedgrower.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("name") var password : String? = null,
    @SerializedName("email") var email : String? = null,
    @SerializedName("password") var name : String? = null
)