package com.example.romanticyeojido.network.locker

data class Photo(
    val id: Int,
    val memory_id: Int,
    val image_url: String,
    val image_order: Int,
    val created_at: String
)
