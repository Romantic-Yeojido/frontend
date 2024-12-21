package com.example.romanticyeojido.ui.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.romanticyeojido.R
import com.example.romanticyeojido.databinding.ActivityMapBinding
import com.example.romanticyeojido.network.PinInterface
import com.example.romanticyeojido.network.PinResponse
import com.example.romanticyeojido.network.NaverAuthInterface
import com.example.romanticyeojido.network.RetrofitClient
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity: AppCompatActivity()  {

    private lateinit var binding: ActivityMapBinding
    private var kakaoMap: KakaoMap? = null
    private var unsavedLabel: Label? = null // 저장되지 않은 라벨
    private lateinit var pinInterface: PinInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 초기화
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegister.isEnabled = false

        // PinInterface 초기화
        pinInterface = RetrofitClient.instance.create(PinInterface::class.java)

        // 액세스 토큰 (예시로 하드코딩) 수정해야함!!!!!!!!!!!!!!!!!
        val accessToken = "your_access_token_here"

        // API 호출
        fetchLocations(accessToken)

        binding.btnRegister.setOnClickListener {
            unsavedLabel?.let { label ->
                val intent = Intent(this, MemoryPostActivity::class.java).apply {
                    putExtra("lat", label.position.latitude)
                    putExtra("lng", label.position.longitude)
                }
                startActivity(intent)
                clearUnsavedLabel() // 라벨 초기화
            } ?: Toast.makeText(this, "핀을 선택해주세요!", Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        initializeMap()
    }

    //API 호출
    private fun fetchLocations(accessToken: String) {
        val call = pinInterface.getLocations("accessToken $accessToken")

        call.enqueue(object : Callback<PinResponse> {
            override fun onResponse(call: Call<PinResponse>, response: Response<PinResponse>) {
                if (response.isSuccessful) {
                    val pinResponse = response.body()
                    if (pinResponse != null && pinResponse.success) {
                        // 성공적으로 데이터를 받았을 경우
                        pinResponse.locations.forEachIndexed { index, location ->
                            Log.d("Location", "위치 $index: 위도: ${location.lat}, 경도: ${location.lng}")

                            val styles = kakaoMap?.labelManager?.addLabelStyles(
                                LabelStyles.from(
                                    LabelStyle.from(R.drawable.ic_pin_gray).setAnchorPoint(0.5f, 1.0f)
                                )
                            )
                            // 지도에 위치 추가
                            val options = LabelOptions.from(LatLng.from(location.lat, location.lng)).setStyles(styles)
                            val layer = kakaoMap?.labelManager?.layer

                            if (layer != null) {
                                val label = layer.addLabel(options)
                                label.show()
                            }
                        }
                    } else {
                        Log.d("API", "응답이 성공적이지 않음")
                    }
                } else {
                    Log.e("API", "응답 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PinResponse>, t: Throwable) {
                Log.e("API", "API 호출 실패", t)
            }
        })
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
        val layer = kakaoMap?.labelManager?.layer

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

        val latitude = label.position.latitude // Label의 위도
        val longitude = label.position.longitude // Label의 경도

        val popupActivity = PopupActivity(this)
        popupActivity.showPopup(binding.root, latitude, longitude) {
            // 버튼 클릭 후 처리할 로직
            val intent = Intent(this, MemoryPostActivity::class.java)
            startActivity(intent)
        }
    }


    private fun clearUnsavedLabel() {
        unsavedLabel?.remove()
        unsavedLabel = null
        binding.btnRegister.isEnabled = false
        binding.btnRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.G000))
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