package com.example.romanticyeojido

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.romanticyeojido.databinding.ActivityMainBinding
import com.example.romanticyeojido.ui.map.MapActivity
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)

        binding.mapCardShortcut.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
    }
}