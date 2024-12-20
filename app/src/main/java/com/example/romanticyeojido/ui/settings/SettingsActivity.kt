package com.example.romanticyeojido.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.romanticyeojido.R
import com.example.romanticyeojido.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.termsOfServiceExpand.setOnClickListener {
            // 이용약관 화면으로 이동
            val intent = Intent(this, TermsOfServiceActivity::class.java)
            startActivity(intent)
        }

        binding.privacyPolicyExpand.setOnClickListener {
            // 이용약관 화면으로 이동
            val intent = Intent(this, PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        binding.locationTermsExpand.setOnClickListener {
            // 이용약관 화면으로 이동
            val intent = Intent(this, LocationTermActivity::class.java)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}