package com.example.romanticyeojido.network


data class PinResponse(
    val success: Boolean,
    val locations: List<LocationData>
)

data class LocationData(
    val lat: Double,
    val lng: Double,
)
