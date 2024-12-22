package com.example.romanticyeojido.ui.map

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.romanticyeojido.R
import com.example.romanticyeojido.databinding.ActivityMapBinding
import com.example.romanticyeojido.network.map.PinInterface
import com.example.romanticyeojido.network.map.PinResponse
import com.example.romanticyeojido.network.map.NewpinInterface
import com.example.romanticyeojido.network.map.NewpinRequest
import com.example.romanticyeojido.network.map.NewpinResponse
import com.example.romanticyeojido.network.RetrofitClient
import com.example.romanticyeojido.network.memoryPost.MemoryContentInterface
import com.example.romanticyeojido.network.memoryPost.MemoryInterface
import com.example.romanticyeojido.network.memoryPost.MemoryService
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
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapActivity: AppCompatActivity()  {

    private lateinit var binding: ActivityMapBinding
    private var kakaoMap: KakaoMap? = null
    private var unsavedLabel: Label? = null // 저장되지 않은 라벨
    private lateinit var pinInterface: PinInterface
    private var latitude: String? = null
    private var longitude: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 초기화
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegister.isEnabled = false

        // PinInterface 초기화
        pinInterface = RetrofitClient.instance.create(PinInterface::class.java)

        //userId 가져오기
        val spf = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = spf.getInt("USER_ID", 0)
        Log.d("MapActivity", "USER_ID in MainActivity: $userId")

        binding.btnRegister.setOnClickListener {
            unsavedLabel?.let { label ->

                sendNewPin(userId,latitude,longitude) // 서버에 핀 정보 전송

                val intent = Intent(this, MemoryPostActivity::class.java).apply{}
                onPause()
                startActivity(intent)
                clearUnsavedLabel() // 라벨 초기화
            } ?: Toast.makeText(this, "핀을 선택해주세요!", Toast.LENGTH_SHORT).show()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        //  val locationId = intent.getIntExtra("locationId", 0)
//        val title = intent.getStringExtra("title")
//        val visit_date = intent.getStringExtra("visit_date")
//        val content = intent.getStringExtra("content")
//        val friends = intent.getStringExtra("friends")
//        val summary = intent.getStringExtra("summary")
//
//        Log.d("MapActivity", "받은 추억 데이터: $title, $visit_date, $content, $friends, $summary")

        initializeMap(userId)
        fetchLocations(userId)
    }

    //API 호출 - 저장된 핀 호출
    fun fetchLocations(userId: Int) {
        val call = pinInterface.getLocations(userId)

        call.enqueue(object : Callback<PinResponse> {
            override fun onResponse(call: Call<PinResponse>, response: Response<PinResponse>) {
                if (response.isSuccessful) {
                    val pinResponse = response.body()
                    if (pinResponse != null && pinResponse.success) {
                        // locations가 null이 아닌지 확인
                        if (pinResponse.result != null) {
                            pinResponse.result.forEachIndexed { index, location ->
                                Log.d("Location", "위치 $index: 위도: ${location.latitude}, 경도: ${location.longitude}")
                                if (location.latitude == null || location.longitude == null) {
                                    Log.e("pinResponse", "위도/경도 값이 null입니다.")
                                }
                            }
                        } else {
                            Log.d("pinResponse", "위치 정보가 없습니다.")
                        }
                    } else {
                        Log.d("pinResponse", "응답이 성공적이지 않음")
                    }
                } else {
                    Log.e("pinResponse", "응답 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PinResponse>, t: Throwable) {
                Log.e("pinResponse", "API 호출 실패", t)
            }
        })
    }

    //지도 설정 함수
    private fun initializeMap(userId: Int) {
        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("onMapDestroy", "onMapDestroy")
                // 지도 API 종료 시 호출
            }

            override fun onMapError(error: Exception) {
                error.printStackTrace() // 오류 로그 출력
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                setupMap() // 지도 설정
                fetchLocations(userId)
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



    // 지도 시작 설정
    private fun setupMap() {
        val position = LatLng.from(37.44983420181418,127.12727640486801)
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(position,15)
        kakaoMap?.moveCamera(cameraUpdate)

        kakaoMap?.setOnMapClickListener { map, position, _, _ ->
            addLabel(position) // 지도 클릭 시 라벨 추가
            Log.d("MapClick", "Clicked Position: ${position.latitude}, ${position.longitude}")
        }
    }

    // 핀 추가
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
            // 새로운 라벨 추가
            val label = layer.addLabel(options)
            label.show()

            // 라벨을 임시 라벨로 설정
            unsavedLabel = label // 새로 생성된 라벨을 임시 라벨로 설정

            latitude = label.position.latitude.toString()
            longitude = label.position.longitude.toString()

            // btnRegister 활성화 및 색 변경
            binding.btnRegister.isEnabled = true
            binding.btnRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.P500))

            // 핀 클릭 리스너
            kakaoMap?.setOnLabelClickListener { map, labelLayer, clickedLabel ->
                initLabelClickListener(map, labelLayer, clickedLabel)
                true
            }

            // 위치 정보 저장
            val spfLoc = getSharedPreferences("map_location", MODE_PRIVATE)
            val editor = spfLoc.edit()
            editor.putString("latitude", label.position.latitude.toString())
            editor.putString("longitude", label.position.longitude.toString())
            editor.apply()
        }
    }

    // 핀 클릭시 반응
    private fun initLabelClickListener(kakaoMap: KakaoMap, layer: LabelLayer, label: Label) {
        Log.d("onLabelClicked", "Clicked Label Position: ${label.position}")

        val spf = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = spf.getInt("USER_ID", 0)

        val locationIdSpf = getSharedPreferences("MapPreferences", MODE_PRIVATE)
        val locationId = locationIdSpf.getInt("locationId", 0)

        fetchMemoryContent(userId, locationId)

        val popupActivity = PopupActivity(this)
        popupActivity.showPopup(binding.root, userId) {
        }
    }

    private fun sendNewPin(userId: Int, latitude: String?, longitude: String?) {
        val apiService = RetrofitClient.instance.create(NewpinInterface::class.java)
        val newpinRequest = NewpinRequest(
            latitude = latitude,
            longitude = longitude
        )
        apiService.postnewpin(userId, newpinRequest).enqueue(object : Callback<NewpinResponse> {
            override fun onResponse(call: Call<NewpinResponse>, response: Response<NewpinResponse>) {
                if (response.isSuccessful) {
                    val pinResponse = response.body()
                    if (pinResponse != null && pinResponse.success) {
                        Log.d("NewpinAPI", "핀 정보 전송 성공: ${pinResponse.result}")
                    } else {
                        Log.e("NewpinAPI", "응답이 성공적이지 않음")
                    }

                    val locationId = pinResponse!!.result.locationId
                    Log.d("NewPinAPI", "핀 정보 전송 성공, LocationId: , $locationId")

                    val spf = getSharedPreferences("MapPreferences", MODE_PRIVATE)
                    val editor = spf.edit()
                    editor.putInt("locationId", locationId)
                    editor.apply()

                    clearUnsavedLabel()

                } else {
                    Log.e("NewpinAPI", "핀 정보 전송 실패: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<NewpinResponse>, t: Throwable) {
                Log.e("NewpinAPI", "API 호출 실패", t)
            }
        })
    }
    private fun clearUnsavedLabel() {
        unsavedLabel?.remove()
        unsavedLabel = null
        binding.btnRegister.isEnabled = false
        binding.btnRegister.setBackgroundColor(ContextCompat.getColor(this, R.color.G300))
    }


    override fun onResume() {
        super.onResume()
        binding.mapView.resume() // MapView resume
        Log.d("MapLog", "Map_resume")

        // 사용자 ID 가져오기
        val spf = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = spf.getInt("USER_ID", 0)
        Log.d("MapActivity", "USER_ID in MainActivity: $userId")

        // 이전에 저장한 좌표
        val spfLoc = getSharedPreferences("map_location", MODE_PRIVATE)
        val latitude = spfLoc.getString("latitude", null)
        val longitude = spfLoc.getString("longitude", null)

//        val memoryspf = getSharedPreferences("MemoryPrefs", MODE_PRIVATE)
//        val title = memoryspf.getString("title", "")
//        val visitDate = memoryspf.getString("visit_date", "")
//        val friends = memoryspf.getString("friends", "")
//        val content = memoryspf.getString("content", "")
//        val summary = memoryspf.getString("summary", "")
//
//        // 추억 데이터 로그 확인
//        Log.d("MapActivity", "추억 데이터: title=$title, visit_date=$visitDate, friends=$friends, content=$content, summary=$summary")

        // 위치가 저장되어 있으면 그곳에 핀 고정
        if (latitude != null && longitude != null) {
            val position = LatLng.from(latitude.toDouble(), longitude.toDouble())
            // 기존 핀 고정 (회색 핀으로)
            val styles = kakaoMap?.labelManager?.addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from(R.drawable.ic_pin_gray).setAnchorPoint(0.5f, 1.0f)
                )
            )

            val options = LabelOptions.from(position).setStyles(styles)
            val layer = kakaoMap?.labelManager?.layer

            if (layer != null) {
                val fixedLabel = layer.addLabel(options)
                fixedLabel.show()
            }
        }

        // 위치 목록 가져오기
        fetchLocations(userId)
    }


    override fun onPause() {
        super.onPause()
        binding.mapView.pause() // MapView pause
        Log.d("MapLog", "Map_pause")
    }

    private fun fetchMemoryContent(userId: Int, locationId: Int) {
        // Create a Retrofit instance and API service
        val apiService = RetrofitClient.instance.create(MemoryContentInterface::class.java)

        // Make the API call
        lifecycleScope.launch {
            try {
                val response = apiService.getMemoryContent(userId, locationId)
                if (response.isSuccessful) {
                    val memoryContent = response.body()?.result
                    if (memoryContent != null) {
                        // Memory data fetched successfully, handle it here
                        Log.d("MemoryData", "Memory Title: ${memoryContent.title}")
                        // You can display it in a Toast or another UI element
                        Toast.makeText(this@MapActivity, "Memory loaded: ${memoryContent.title}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("MemoryData", "Error fetching memory data: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("MemoryData", "API call failed", e)
            }
        }
    }

}