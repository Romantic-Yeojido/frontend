package com.example.romanticyeojido.ui.memoryPost

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.romanticyeojido.R
import com.example.romanticyeojido.databinding.ActivityMemoryPostBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel

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
}