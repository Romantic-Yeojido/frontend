package com.example.romanticyeojido

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // 로딩 이미지를 불러오고 애니메이션 적용
        val loadingImage = findViewById<ImageView>(R.id.loadingImage)
        val rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation)
        loadingImage.startAnimation(rotateAnimation)
    }
}
