package com.example.romanticyeojido.ui.memoryPost

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.romanticyeojido.R
import com.example.romanticyeojido.databinding.ActivityMemoryPostBinding
import com.example.romanticyeojido.network.RetrofitClient
import com.example.romanticyeojido.network.memoryPost.ImageService
import com.example.romanticyeojido.network.memoryPost.MemoryRequest
import com.example.romanticyeojido.network.memoryPost.MemoryResponse
import com.example.romanticyeojido.network.memoryPost.MemoryService
import com.example.romanticyeojido.ui.map.MapActivity
import com.example.romanticyeojido.utils.createImageMultipart
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import java.util.UUID

class MemoryPostActivity: AppCompatActivity() {

    private lateinit var binding : ActivityMemoryPostBinding

    val years = listOf("년도", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027")
    val months = listOf("월", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    val days = listOf("날짜", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18",
        "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31")

    companion object {
        private const val REQUEST_CODE_SELECT_IMAGES = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //binding 초기화
        binding = ActivityMemoryPostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //좌표값 받기
//        val intent = intent
//        lat = intent.getDoubleExtra("lat", 0.0)
//        lng = intent.getDoubleExtra("lng", 0.0)

        val spf = getSharedPreferences("map_location", MODE_PRIVATE)
        val lat = spf.getString("lat", "")
        val lng = spf.getString("lng", "")

        Log.d("MemoryPostActivity", "lat: $lat")
        Log.d("MemoryPostActivity", "lng: $lng")

        if (lat != null && lng != null) {
            Log.d("MemoryPostActivity", "위도: $lat, 경도: $lng")
        }

//        // RecyclerView 초기화
//        binding.dropdownRv.layoutManager = LinearLayoutManager(this)
//        binding.dropdownRv.adapter = DropdownAdapter(years) { selectedItem ->
//            binding.textRvBtn.text = selectedItem // 선택한 항목으로 TextView 업데이트
//        }
//
//        // TextView 클릭 이벤트
//        binding.dropdownBtn.setOnClickListener {
//            val visibility = if (binding.testSv.visibility == View.VISIBLE) {
//                View.GONE
//            } else {
//                View.VISIBLE
//            }
//            binding.testSv.visibility = visibility
//        }

        //Spinner Adapter 초기화
        val yearAdapter = SpinnerAdapter(this, years)
        binding.postYearOptionDd.adapter = yearAdapter

        val monthAdapter = SpinnerAdapter(this, months)
        binding.postMonthOptionDd.adapter = monthAdapter

        val dayAdapter = SpinnerAdapter(this, days)
        binding.postDayOptionDd.adapter = dayAdapter

        //Spinner 이벤트 처리
        binding.postYearOptionDd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedView: View?, position: Int, id: Long) {
                // 선택된 항목을 표시
                val selectedItem = parentView.getItemAtPosition(position) as String
                if (selectedItem != "년도") {
                    binding.postYearOptionDd.setBackgroundResource(R.drawable.bg_spinner_active) // 첫 번째 선택 이후부터 유효성 검사 활성화
                } else {
                    binding.postYearOptionDd.setBackgroundResource(R.drawable.bg_spinner_default)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                binding.postYearOptionDd.setBackgroundResource(R.drawable.bg_spinner_default)
            }
        }

        binding.postMonthOptionDd.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedView: View?, position: Int, id: Long) {
                // 선택된 항목을 표시
                val selectedItem = parentView.getItemAtPosition(position) as String
                if (selectedItem != "월") {
                    binding.postMonthOptionDd.setBackgroundResource(R.drawable.bg_spinner_active) // 첫 번째 선택 이후부터 유효성 검사 활성화
                } else {
                    binding.postMonthOptionDd.setBackgroundResource(R.drawable.bg_spinner_default)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.postDayOptionDd.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedView: View?, position: Int, id: Long) {
                // 선택된 항목을 표시
                val selectedItem = parentView.getItemAtPosition(position) as String
                if (selectedItem != "날짜") {
                    binding.postDayOptionDd.setBackgroundResource(R.drawable.bg_spinner_active) // 첫 번째 선택 이후부터 유효성 검사 활성화
                } else {
                    binding.postDayOptionDd.setBackgroundResource(R.drawable.bg_spinner_default)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        initTextWatcher()

        //클릭 리스너 초기화
        initClickListener()
    }

    private fun initClickListener() {
        binding.postRegisterBtn.setOnClickListener {
            // 버튼 클릭 시 유효성 검사 실행
            validateInputs()
            if (binding.postRegisterBtn.isEnabled == true) {
                postMemory()
                finish()
            }
        }

        binding.postIcBackBtn.setOnClickListener {
            finish()
        }

        binding.postAddImgBtn.setOnClickListener {
            openGallery()
        }
    }

    private fun initTextWatcher() {
        // 각 EditText에 TextWatcher 추가
        binding.postTitleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // 텍스트가 변경되기 전 처리 (필요시 구현)
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경 중일 때 처리 (필요시 구현)
            }

            override fun afterTextChanged(editable: Editable?) {
                // 텍스트가 변경된 후 유효성 검사 실행
                validateInputs()
            }
        })

        binding.postPeopleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // 텍스트가 변경되기 전 처리 (필요시 구현)
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경 중일 때 처리 (필요시 구현)
            }

            override fun afterTextChanged(editable: Editable?) {
                // 텍스트가 변경된 후 유효성 검사 실행
                validateInputs()
            }
        })

        binding.postContentEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // 텍스트가 변경되기 전 처리 (필요시 구현)
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경 중일 때 처리 (필요시 구현)
            }

            override fun afterTextChanged(editable: Editable?) {
                // 텍스트가 변경된 후 유효성 검사 실행
                validateInputs()
            }
        })
    }

    private fun validateInputs() {
        // Title 유효성 검사
        if (binding.postTitleEt.text.toString().isEmpty()) {
            binding.postTitleEt.setBackgroundResource(R.drawable.edittext_error)
            binding.postTitleErrorTv.visibility = View.VISIBLE
        } else {
            binding.postTitleEt.setBackgroundResource(R.drawable.edittext_focused)
            binding.postTitleErrorTv.visibility = View.GONE
        }

        // People 유효성 검사
        if (binding.postPeopleEt.text.toString().isEmpty()) {
            binding.postPeopleEt.setBackgroundResource(R.drawable.edittext_error)
            binding.postPeopleErrorTv.visibility = View.VISIBLE
        } else {
            binding.postPeopleEt.setBackgroundResource(R.drawable.edittext_focused)
            binding.postPeopleErrorTv.visibility = View.GONE
        }

        // Content 유효성 검사
        if (binding.postContentEt.text.toString().isEmpty()) {
            binding.postContentEt.setBackgroundResource(R.drawable.edittext_error)
            binding.postContentErrorTv.visibility = View.VISIBLE
        } else {
            binding.postContentEt.setBackgroundResource(R.drawable.edittext_focused)
            binding.postContentErrorTv.visibility = View.GONE
        }

        // 버튼 활성화 여부 체크
        updateButtonState()
    }

    private fun updateButtonState() {
        // 모든 EditText가 비어있지 않으면 버튼 활성화
        val isFormValid = binding.postTitleEt.text.isNotEmpty() &&
                binding.postPeopleEt.text.isNotEmpty() &&
                binding.postContentEt.text.isNotEmpty()

        if (isFormValid) {
            binding.postRegisterBtn.isEnabled = true
            binding.postRegisterBtn.setBackgroundResource(R.drawable.bg_register_btn_active)
        } else {
            binding.postRegisterBtn.isEnabled = false
            binding.postRegisterBtn.setBackgroundResource(R.drawable.bg_register_btn_default)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGES)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGES && resultCode == RESULT_OK) {
            val clipData = data?.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    addImageToScrollView(imageUri)
                }
            } else {
                data?.data?.let { imageUri ->
                    addImageToScrollView(imageUri)
                }
            }
        }
    }

    private fun addImageToScrollView(imageUri: Uri) {
        // 새로운 ImageView 생성
        val imageView = ShapeableImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(100.dpToPx(), 100.dpToPx()).apply {
                marginEnd = 10.dpToPx()
            }
            setImageURI(imageUri)
            scaleType = ImageView.ScaleType.CENTER_CROP
            shapeAppearanceModel = ShapeAppearanceModel.builder()
                .setAllCorners(CornerFamily.ROUNDED, 10.dpToPx().toFloat())
                .build()
        }

        // LinearLayout에 추가
        binding.postImgHv.addView(imageView)

        // '이미지 추가 버튼'을 맨 뒤로 이동
        binding.postImgHv.removeView(binding.postAddImgBtn)
        binding.postImgHv.addView(binding.postAddImgBtn)
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    //추억 등록하기 함수
    private fun postMemory() {

        //userId 받아오기
        val userIdSpf = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = userIdSpf.getInt("USER_ID", 0)
        Log.d("MemoryPostActivity", "USER_ID in MemoryPostActivity: $userId")

        //locationId 받아오기
        val locationIdspf = getSharedPreferences("MapPreferences", MODE_PRIVATE)
        val locationId = locationIdspf.getInt("locationId", 0)

        if (locationId != -1) {
            Log.d("MemoryPostActivity", "받은 locationId: $locationId")
            // locationId를 사용하여 추억 등록 관련 작업을 진행합니다.
        } else {
            Log.e("MemoryPostActivity", "locationId가 저장되지 않았습니다.")
        }

        //작성한 데이터 변수 선언
        val title = binding.postTitleEt.text.toString()
        val visitDate = "${binding.postYearOptionDd.selectedItem}-${binding.postMonthOptionDd.selectedItem}-${binding.postDayOptionDd.selectedItem}"
        val friends = binding.postPeopleEt.text.toString()
        val content = binding.postContentEt.text.toString()

        //memoryId 선언
        val memoryId = UUID.randomUUID().toString()
        Log.d("MemoryPostActivity", "memoryId: ${memoryId}")

        val memoryRequest = MemoryRequest(
            title = title,
            visit_date = visitDate,
            friends = friends,
            content = content
        )

        val memoryService = RetrofitClient.instance.create(MemoryService::class.java)
        memoryService.postMemory(userId, locationId, memoryRequest).enqueue(object : retrofit2.Callback<MemoryResponse> {
            override fun onResponse(call: Call<MemoryResponse>, response: retrofit2.Response<MemoryResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()?.result
                    Log.d("MemoryPostActivity", "추억 등록 성공: $result")
                    // 성공 알림 또는 화면 이동 처리

                    // 추억 등록 후, 맵 액티비티로 locationId 전송
                    val intent = Intent(this@MemoryPostActivity, MapActivity::class.java)
                    intent.putExtra("locationId", locationId)  // locationId 전달
//                    intent.putExtra("title", response.body()?.result?.title)
//                    intent.putExtra("visit_date", response.body()?.result?.visit_date)
//                    intent.putExtra("content", response.body()?.result?.content)
//                    intent.putExtra("friends", response.body()?.result?.friends)
//                    intent.putExtra("summary", response.body()?.result?.summary)
                } else {
                    Log.e("MemoryPostActivity", "추억 등록 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<MemoryResponse>, t: Throwable) {
                Log.e("MemoryPostActivity", "추억 등록 에러: ${t.message}")
            }
        })
    }

    private fun uploadImages(memoryId: Int, imageUris: List<Uri>) {
        val imageParts = mutableListOf<MultipartBody.Part>()

        // Uri 리스트를 MultipartBody.Part로 변환
        for ((index, uri) in imageUris.withIndex()) {
            createImageMultipart(this, uri, "images[$index]")?.let {
                imageParts.add(it)
            }
        }

        val imageService = RetrofitClient.instance.create(ImageService::class.java)
        imageService.uploadImages(memoryId, imageParts)
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Log.d("MemoryPostActivity", "이미지 업로드 성공")
                    } else {
                        Log.e("MemoryPostActivity", "이미지 업로드 실패: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("MemoryPostActivity", "이미지 업로드 에러: ${t.message}")
                }
            })

//        override fun onResponse(call: Call<MemoryResponse>, response: retrofit2.Response<MemoryResponse>) {
//            if (response.isSuccessful) {
//                val memoryId = response.body()?.result?.id
//                Log.d("MemoryPostActivity", "추억 등록 성공: $memoryId")
//
//                if (memoryId != null) {
//                    uploadImages(memoryId, selectedImageUris) // selectedImageUris는 선택된 이미지의 Uri 리스트
//                }
//            } else {
//                Log.e("MemoryPostActivity", "추억 등록 실패: ${response.errorBody()?.string()}")
//            }
//        }
//
//        private val selectedImageUris = mutableListOf<Uri>()
//
//        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//            super.onActivityResult(requestCode, resultCode, data)
//            if (requestCode == REQUEST_CODE_SELECT_IMAGES && resultCode == RESULT_OK) {
//                val clipData = data?.clipData
//                if (clipData != null) {
//                    for (i in 0 until clipData.itemCount) {
//                        val imageUri = clipData.getItemAt(i).uri
//                        selectedImageUris.add(imageUri)
//                        addImageToScrollView(imageUri)
//                    }
//                } else {
//                    data?.data?.let { imageUri ->
//                        selectedImageUris.add(imageUri)
//                        addImageToScrollView(imageUri)
//                    }
//                }
//            }
//        }
//    }
//
//
    }
}