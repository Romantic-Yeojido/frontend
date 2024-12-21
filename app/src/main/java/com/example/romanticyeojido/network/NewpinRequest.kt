package com.example.romanticyeojido.network

import com.google.gson.annotations.SerializedName

data class NewpinRequest(
    @SerializedName("latitude") var latitude: String?,
    @SerializedName("longitude") var longitude: String?
)
