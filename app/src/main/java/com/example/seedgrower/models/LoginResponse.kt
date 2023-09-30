package com.example.seedgrower.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("ok") var ok : Boolean? = null,
    @SerializedName("token") var token : String? = null,
)