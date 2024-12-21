package com.example.romanticyeojido.network.memoryPost

import com.example.romanticyeojido.network.PopupResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MemoryInterface {
    @GET("api/v1/users/:userId/map/memory")
    fun getPopupData(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): Call<PopupResponse>
}