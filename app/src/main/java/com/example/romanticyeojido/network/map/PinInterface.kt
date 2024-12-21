package com.example.romanticyeojido.network.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


interface PinInterface {
    @GET("/api/v1/users/{userId}/map") // 요청 경로
    fun getLocations(
        @Path("userId") userId: Int,
    ): Call<PinResponse>
}