package com.example.seedgrower.models

import com.google.gson.annotations.SerializedName

data class Token (
    @SerializedName("token") var token : String? = null,
)