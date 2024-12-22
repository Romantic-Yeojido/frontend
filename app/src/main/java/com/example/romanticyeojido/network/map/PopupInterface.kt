package com.example.romanticyeojido.network.map

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PopupInterface {
    @GET("/api/v1/users/{userId}/map/memory") // 요청 경로
    fun getPopupData(
        @Path("userId") userId: Int,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String

    ): Call<PopupResponse>
}