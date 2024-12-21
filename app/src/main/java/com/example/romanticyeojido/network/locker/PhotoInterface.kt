package com.example.romanticyeojido.network.locker

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PhotoInterface {
    @Multipart
    @POST("api/v1/users/memories/{memoryId}/images") // {memoryId}로 수정
    fun postUserPhotos(
        @Header("Authorization") token: String, // Authorization 헤더에 추가할 토큰
        @Path("memoryId") memoryId: Int,       // memoryId를 Path로 추가
        @Part image: MultipartBody.Part
    ): Call<List<Photo>>


    @GET("api/v1/users/memories/{memoryId}/images") // {memoryId}로 수정
    fun getUserPhotos(
        @Header("Authorization") token: String,  // Authorization 헤더에 추가할 토큰
        @Path("memoryId") memoryId: String          // memoryId를 Path로 추가
    ): Call<List<Photo>>

}