package com.example.romanticyeojido.network

import com.google.gson.annotations.SerializedName

data class AuthResponse(
     val auth_url : String,
     val state : String,
)
