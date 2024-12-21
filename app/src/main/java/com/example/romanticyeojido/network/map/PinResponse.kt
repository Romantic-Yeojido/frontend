package com.example.romanticyeojido.network.map


data class PinResponse(
    val success: Boolean,
    val locations: List<LocationData>
)

data class LocationData(
    val latitude: Double,
    val longitude: Double,
)
