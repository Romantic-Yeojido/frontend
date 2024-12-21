package com.example.romanticyeojido.network

data class NewpinResponse(
    val success: Boolean,
    val result: Newlocation
)
data class Newlocation(
    val locationId: Int,
    val latitude: String?,
    val longitude: String?
)
