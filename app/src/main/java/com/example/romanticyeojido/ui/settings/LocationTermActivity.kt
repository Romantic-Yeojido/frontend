package com.example.romanticyeojido.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.romanticyeojido.databinding.ActivityLocationTermsBinding

class LocationTermActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationTermsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationTermsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}