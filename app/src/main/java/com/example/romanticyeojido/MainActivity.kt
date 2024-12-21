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

        // 웹 뷰에서 메인 액티비티로 유저 아이디 받아오기
        val spf = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = spf.getInt("USER_ID", 0)
        Log.d("MainActivity", "USER_ID in MainActivity: $userId")

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