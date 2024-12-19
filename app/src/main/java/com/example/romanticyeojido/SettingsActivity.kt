package com.example.romanticyeojido

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.romanticyeojido.databinding.ActivitySettingsBinding

class SettingsActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 초기화
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}