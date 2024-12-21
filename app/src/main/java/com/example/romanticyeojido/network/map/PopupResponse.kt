package com.example.romanticyeojido.network.map


data class PopupResponse(
    val success: Boolean,
    val memory: MemoryData
)

data class MemoryData(
    val id: Int,
    val title: String,
    val visit_date: String,
    val friends: String,
    val gpt_summary: String,
    val image_url: String
)
