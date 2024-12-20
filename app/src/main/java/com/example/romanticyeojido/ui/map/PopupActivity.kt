package com.example.romanticyeojido.ui.map

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.example.romanticyeojido.R
import com.example.romanticyeojido.databinding.ItemMappopupBinding
import com.example.romanticyeojido.network.BASE_URL
import com.example.romanticyeojido.network.MemoryInterface
import com.example.romanticyeojido.network.PopupResponse
import com.example.romanticyeojido.network.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopupActivity (private val context: Context) {

    fun showPopup(anchorView: ViewGroup, latitude: Double, longitude: Double, onCircleButtonClick: () -> Unit) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.item_mappopup, null)
        val binding = ItemMappopupBinding.bind(popupView)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isFocusable = true
            isOutsideTouchable = true
        }

        popupWindow.showAtLocation(anchorView, Gravity.BOTTOM, 0, 20)

        getPopupData(binding, latitude, longitude)


        binding.circleBtn.setOnClickListener {
            onCircleButtonClick.invoke()
            popupWindow.dismiss()
        }
    }

    private fun getPopupData(binding: ItemMappopupBinding, latitude: Double, longitude: Double) {
        val apiService = getRetrofit().create(MemoryInterface::class.java)
        apiService.getPopupData(latitude, longitude).enqueue(object : Callback<PopupResponse> {
            override fun onResponse(call: Call<PopupResponse>, response: Response<PopupResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.memory?.let { memory ->
                        // 데이터 바인딩
                        binding.tvPopupTitle.text = memory.title
                        binding.tvPopupDate.text = "방문일: ${memory.visit_date} / 친구: ${memory.friends}"
                        binding.tvPopupDescription.text = memory.gpt_summary
                        Glide.with(binding.imgPopup.context)
                            .load(BASE_URL + memory.image_url) // URL 경로 보정
                            .into(binding.imgPopup)
                    }
                } else {
                    binding.tvPopupTitle.text = "데이터를 불러올 수 없습니다."
                    binding.tvPopupDate.text = "데이터를 불러올 수 없습니다."
                    binding.tvPopupDescription.text = "데이터를 불러올 수 없습니다."
                }
            }

            override fun onFailure(call: Call<PopupResponse>, t: Throwable) {
                binding.tvPopupTitle.text = "데이터를 불러올 수 없습니다."
                binding.tvPopupDate.text = "데이터를 불러올 수 없습니다."
                binding.tvPopupDescription.text = "데이터를 불러올 수 없습니다."
            }
        })
    }
}
