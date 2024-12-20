package com.example.romanticyeojido.ui.map

import android.content.Intent
import android.content.SharedPreferences
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
import androidx.core.content.ContextCompat
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
    private val savedLabels: MutableList<Label> = mutableListOf() // 저장된 라벨 리스트
    private var unsavedLabel: Label? = null // 저장되지 않은 라벨


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 초기화
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegister.isEnabled = false

        binding.btnRegister.setOnClickListener {
            saveLabelData() // 라벨 데이터 저장
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

        kakaoMap?.setOnMapClickListener { map, position, _, _ ->
            addLabel(position) // 지도 클릭 시 라벨 추가
            Log.d("MapClick", "Clicked Position: ${position.latitude}, ${position.longitude}")
        }
    }

    private fun addLabel(position: LatLng) {
        // 기존 임시 라벨 제거
        unsavedLabel?.remove()

        val styles = kakaoMap?.labelManager?.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from(R.drawable.ic_pin).setAnchorPoint(0.5f, 1.0f)
            )
        )

        val options = LabelOptions.from(position).setStyles(styles)
        val labelManager = kakaoMap?.labelManager
        val layer = labelManager?.layer

        if (layer != null) {
            val label = layer.addLabel(options)
            label.show()
            unsavedLabel = label // 새로 생성된 라벨을 임시 라벨로 설정


            // btnRegister 활성화 및 색 변경
            binding.btnRegister.isEnabled = true
            binding.btnRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.P500))


            kakaoMap?.setOnLabelClickListener { map, labelLayer, clickedLabel ->
                initLabelClickListener(map, labelLayer, clickedLabel)
                true
            }
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

    private fun saveLabelData() {
        unsavedLabel?.let { label ->
            val sharedPreferences = getSharedPreferences("MapPreferences", MODE_PRIVATE)
            val labelSet = getStoredLabels(sharedPreferences).toMutableSet()
            labelSet.add("${label.position.latitude},${label.position.longitude}")
            with(sharedPreferences.edit()) {
                putStringSet("stored_labels", labelSet)
                apply()
            }

            // 저장된 라벨 리스트에 추가하고 임시 라벨 초기화
            savedLabels.add(label)
            unsavedLabel = null
        }

    }


    private fun getStoredLabels(sharedPreferences: SharedPreferences): Set<String> {
        return sharedPreferences.getStringSet("stored_labels", emptySet()) ?: emptySet()
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