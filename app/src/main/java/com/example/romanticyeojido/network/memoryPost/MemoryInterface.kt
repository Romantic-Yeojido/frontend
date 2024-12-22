package com.example.romanticyeojido.network.memoryPost

import com.example.romanticyeojido.network.map.PopupResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MemoryInterface {
    @GET("api/v1/users/:userId/map/memory")
    fun getPopupData(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double
    ): Call<PopupResponse>
}

interface MemoryService {
    @POST("/api/v1/users/{userId}/locations/{location}")
    fun postMemory(
        @Path("userId") userId: Int,
        @Path("location") location: Int,
        @Body memoryRequest: MemoryRequest
    ): Call<MemoryResponse>
}



interface ImageService {
    @Multipart
    @POST("/api/v1/users/memories/{memoryId}/images")
    fun uploadImages(
        @Path("memoryId") memoryId : Int,
        @Part images: List<MultipartBody.Part>
    ): Call<ResponseBody>
}

interface MemoryContentInterface {
    @GET("/api/v1/users/{userId}/locations/{locationId}/memory-content")
    suspend fun getMemoryContent(
        @Path("userId") userId: Int,
        @Path("locationId") locationId: Int
    ): Response<MemoryContentResponse>
}