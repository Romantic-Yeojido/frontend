package com.example.romanticyeojido

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoMapSdk.init(this, "32f3df67980ad78b1db71910ef1193cc")
    }
}