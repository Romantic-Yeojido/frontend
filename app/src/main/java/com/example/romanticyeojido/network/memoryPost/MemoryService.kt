package com.example.romanticyeojido.network.memoryPost

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

data class MemoryRequest(
    @SerializedName(value = "title") val title: String?,
    @SerializedName(value = "visit_date") val visit_date: String?,
    @SerializedName(value = "friends") val friends: String?,
    @SerializedName(value = "content") val content: String,
)

data class MemoryResponse(
    @SerializedName(value = "result") val result: MemoryResult
)

data class MemoryResult(
    @SerializedName(value = "user_id") val user_id: Int,
    @SerializedName(value = "location_id") val location_id: Int,
    @SerializedName(value = "title") val title: String,
    @SerializedName(value = "visit_date") val visit_date: String,
    @SerializedName(value = "friends") val friends: String,
    @SerializedName(value = "content") val content: String,
    @SerializedName(value = "summary") val summary: String
)

data class MemoryContentResponse(
    val result: MemoryContent
)

data class MemoryContent(
    val id: Int,
    val user_id: Int,
    val location_id: Int,
    val title: String,
    val visit_date: String,
    val friends: String,
    val content: String,
    val summary: String,
    val is_deleted: Int,
    val created_at: String,
    val updated_at: String
)

data class ErrorResponse(
    val success: Boolean,
    val message: String
)