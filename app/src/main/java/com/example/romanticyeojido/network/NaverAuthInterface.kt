package com.example.romanticyeojido.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface NaverAuthInterface {
    @GET("/api/v1/users/oauth2/login/naver")
    fun getAuthUrl(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String = "application/json"
    ): Call<AuthResponse>
}