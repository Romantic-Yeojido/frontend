package com.example.romanticyeojido

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.romanticyeojido.databinding.ActivityMainBinding
import com.example.romanticyeojido.ui.locker.LockerActivity
import com.example.romanticyeojido.ui.map.MapActivity
import com.kakao.sdk.common.util.Utility
import com.example.romanticyeojido.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)

        val userName = intent.getStringExtra("USER_NAME")
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userId = intent.getStringExtra("USER_ID")

        val isApiSuccess = intent.getBooleanExtra("API_SUCCESS", false)  // 기본값은 false

        if (isApiSuccess) {
            Toast.makeText(this, "API 통신 성공", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "API 통신 실패", Toast.LENGTH_SHORT).show()
        }

        Log.d("MainActivity", "User Name: $userName")
        Log.d("MainActivity", "User Email: $userEmail")
        Log.d("MainActivity", "User Id: $userId")

        initOnClickListener()
    }



    private fun initOnClickListener() {
        binding.mapCardShortcut.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        binding.settingCardShortcut.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        binding.treasureBoxIv.setOnClickListener {
            startActivity(Intent(this, LockerActivity::class.java))
        }
    }
}