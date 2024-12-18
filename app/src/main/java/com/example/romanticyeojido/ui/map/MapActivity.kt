package com.example.romanticyeojido.ui.map

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.romanticyeojido.databinding.ActivityMapBinding
import com.example.romanticyeojido.ui.memoryPost.MemoryPostActivity

class MapActivity: AppCompatActivity()  {

    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 초기화
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, MemoryPostActivity::class.java))
        }
    }
}