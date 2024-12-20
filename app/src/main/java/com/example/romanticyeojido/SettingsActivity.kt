package com.example.romanticyeojido

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // activity_settings.xml에서 terms_of_service_expand 뷰를 찾음
        val termsOfServiceExpand: ImageView = findViewById(R.id.terms_of_service_expand)
        // 클릭 이벤트 설정
        termsOfServiceExpand.setOnClickListener {
            // 이용약관 화면으로 이동
            val intent = Intent(this, TermsOfServiceActivity::class.java)
            startActivity(intent)
        }

        // activity_settings.xml에서 terms_of_service_expand 뷰를 찾음
        val PrivacyPolicyExpand: ImageView = findViewById(R.id.privacy_policy_expand)
        // 클릭 이벤트 설정
        PrivacyPolicyExpand.setOnClickListener {
            // 이용약관 화면으로 이동
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        // activity_settings.xml에서 terms_of_service_expand 뷰를 찾음
        val LocationTermExpand: ImageView = findViewById(R.id.location_terms_expand)
        // 클릭 이벤트 설정
        LocationTermExpand.setOnClickListener {
            // 이용약관 화면으로 이동
            val intent = Intent(this, LocationTermActivity::class.java)
            startActivity(intent)
        }
    }
}