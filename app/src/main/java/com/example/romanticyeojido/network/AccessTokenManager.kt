package com.example.romanticyeojido.network

object AccessTokenManager {
    private var accessToken: String? = null

    // 액세스 토큰을 저장하는 메서드
    fun setAccessToken(token: String?) {
        accessToken = token
    }

    // 액세스 토큰을 가져오는 메서드
    fun getAccessToken(): String? {
        return accessToken
    }
}