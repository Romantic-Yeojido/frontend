package com.example.romanticyeojido.ui.locker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.romanticyeojido.databinding.ActivityLockerBinding
import com.example.romanticyeojido.ui.memoryPosts.MemoryPostActivity

class LockerActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLockerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLockerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEdit.setOnClickListener {
            startActivity(Intent(this, MemoryPostActivity::class.java))
        }

        binding.layoutVp.setOnClickListener {
            startActivity(Intent(this, LockerPictureActivity::class.java))
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}