package com.example.romanticyeojido.network

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName(value = "auth_url") val auth_url : String,
    @SerializedName(value = "state") val state : String,
)
