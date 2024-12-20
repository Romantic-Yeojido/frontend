package com.example.romanticyeojido.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header


interface PinInterface {
    @GET("api/v1/users/:userId/map") // 요청 경로
    fun getLocations(
        @Header("Authorization") accessToken: String // Authorization 헤더를 요청에 추가
    ): Call<PinResponse>
}