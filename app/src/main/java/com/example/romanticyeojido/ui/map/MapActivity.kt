package com.example.romanticyeojido.ui.map

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.romanticyeojido.R
import com.example.romanticyeojido.databinding.ActivityMapBinding
import com.example.romanticyeojido.databinding.ItemMappopupBinding
import com.example.romanticyeojido.ui.memoryPost.MemoryPostActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
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

        binding.btnBack.setOnClickListener {
            finish()
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
        val position = LatLng.from(37.44983420181418,127.12727640486801)
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(position,15)
        kakaoMap?.moveCamera(cameraUpdate)

        settingLabel()
        }

    private fun settingLabel(){
        val styles = kakaoMap?.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_pin)
            .setAnchorPoint(0.5f,0.5f)))

        val options = LabelOptions.from(LatLng.from(37.44983420181418,127.12727640486801))
            .setStyles(styles)


        val labelManager = kakaoMap?.labelManager
        val layer = labelManager?.layer

        if(layer != null) {
            val label = layer.addLabel(options)
            label.show()

            kakaoMap?.setOnLabelClickListener { map, labelLayer, clickedLabel ->
                initLabelClickListener(map, labelLayer, clickedLabel)
                true // 이벤트 처리 완료를 의미
            }

            Log.d("test","${label}")
        }else{

        }
    }

    private fun initLabelClickListener(kakaoMap: KakaoMap, layer: LabelLayer, label: Label) {
        Log.d("onLabelClicked", "Clicked Label Position: ${label.position}")

        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.item_mappopup, null)
        val binding = ItemMappopupBinding.bind(popupView)

        // 팝업 윈도우 설정
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isFocusable = true
            isOutsideTouchable = true // 외부 터치 허용
        }
        val anchorView = binding.root // MapView를 기준으로 위치 설정
        popupWindow.showAtLocation(anchorView, Gravity.BOTTOM,0,20)

        binding.circleBtn.setOnClickListener {
            val intent = Intent(this, MemoryPostActivity::class.java)
            startActivity(intent)
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