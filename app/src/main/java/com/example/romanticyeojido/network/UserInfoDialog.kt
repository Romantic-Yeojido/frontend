package com.example.romanticyeojido.network

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.romanticyeojido.databinding.DialogUserInfoBinding
import com.squareup.picasso.Picasso

class UserInfoDialog(context: Context, private val nickname: String?, private val profileImageUrl: String?, private val email: String?) : Dialog(context) {

    private lateinit var binding: DialogUserInfoBinding
    private var onConfirmListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 사용자 정보 설정
        binding.kakaoLoginNicknameTv.text = nickname
        binding.kakaoLoginEmailTv.text = email ?: "이메일 정보 없음"

        // 프로필 이미지 로드
        if (profileImageUrl != null) {
            Picasso.get().load(profileImageUrl).into(binding.kakaoLoginProfileImgIv)
        }

        // CONFIRM 버튼 클릭 시
        binding.kakaoLoginBtn.setOnClickListener {
            onConfirmListener?.invoke()  // 리스너 호출
            dismiss()  // 다이얼로그 닫기
        }
    }

    fun setOnConfirmListener(listener: () -> Unit) {
        this.onConfirmListener = listener
    }
}