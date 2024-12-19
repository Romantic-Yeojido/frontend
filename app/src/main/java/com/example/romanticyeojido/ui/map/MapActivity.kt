package com.example.romanticyeojido.ui.map

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.romanticyeojido.R
import com.example.romanticyeojido.databinding.ActivityMapBinding
import com.example.romanticyeojido.ui.memoryPost.MemoryPostActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapActivity: AppCompatActivity()  {

    private lateinit var binding: ActivityMapBinding
    private var kakaoMap: KakaoMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 초기화
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, MemoryPostActivity::class.java))
        }
        initializeMap()
    }

    private fun initializeMap() {
        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 종료 시 호출
            }

            override fun onMapError(error: Exception) {
                error.printStackTrace() // 오류 로그 출력
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                setupMap() // 지도 설정
            }

            override fun getPosition(): LatLng {
                return LatLng.from(37.406960, 127.115587) // 초기 위치 설정
            }

            override fun getZoomLevel(): Int {
                return 15 // 초기 줌 레벨 설정
            }

            override fun isVisible(): Boolean {
                return true // 지도 표시 여부
            }
        })
    }

    private fun setupMap() {
        kakaoMap?.let { map ->
            // 마커 생성
            addMarker(map, LatLng.from(37.394660, 127.111182), R.drawable.ic_symbol)
        }
    }

    private fun addMarker(map: KakaoMap, position: LatLng, iconRes: Int) {
        // 1. LabelStyles 생성
        val labelManager = map.getLabelManager()
        if (labelManager != null) {
            val labelStyles = labelManager.addLabelStyles(LabelStyles.from(LabelStyle.from(iconRes)))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume() // MapView resume
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause() // MapView pause
    }
}