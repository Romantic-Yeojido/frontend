package com.example.romanticyeojido.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NewpinInterface {
    @POST("/api/v1/users/{userId}/map/new-pin") // 요청 경로
        fun postnewpin(
            @Path("userId") userId: Int,
            @Body newpinRequest: NewpinRequest

    ): Call<NewpinResponse>
    }