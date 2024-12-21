package com.example.romanticyeojido.network

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val BASE_URL = "http://3.37.26.60:3000"

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
// AccessTokenManager에서 액세스 토큰을 가져옴
            val accessToken = AccessTokenManager.getAccessToken()

            if (accessToken != null) {
                // Authorization 헤더에 토큰 추가
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken") // "Bearer"와 함께 토큰 추가
                    .addHeader("Content-Type", "application/json")
                    .build()
                return@Interceptor chain.proceed(request)
            } else {
                // 토큰이 없으면 그냥 요청 진행
                return@Interceptor chain.proceed(chain.request())
            }
        })
        .build()

    val gson = GsonBuilder()
        .setLenient()  // Lenient 모드를 활성화
        .create()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}