package com.example.romanticyeojido.network.map

import com.google.gson.annotations.SerializedName

data class NewpinRequest(
    @SerializedName("latitude") var latitude: String?,
    @SerializedName("longitude") var longitude: String?
)
