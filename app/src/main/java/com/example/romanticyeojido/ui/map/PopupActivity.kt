package com.example.romanticyeojido.ui.map

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.example.romanticyeojido.R
import com.example.romanticyeojido.databinding.ItemMappopupBinding
import com.example.romanticyeojido.network.AccessTokenManager
import com.example.romanticyeojido.network.PopupInterface
//import com.example.romanticyeojido.network.BASE_URL
import com.example.romanticyeojido.network.memoryPost.MemoryInterface
import com.example.romanticyeojido.network.PopupResponse
import com.example.romanticyeojido.network.RetrofitClient
import com.example.romanticyeojido.network.RetrofitClient.BASE_URL
//import com.example.romanticyeojido.network.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopupActivity (private val context: Context) {

    fun showPopup(anchorView: ViewGroup, userId: Int, onCircleButtonClick: () -> Unit) {
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

        val (latitude, longitude) = getMapLocation()
        getPopupData(binding, userId, latitude, longitude)
        Log.d("PopupActivity", "USER_ID in PopupActivity: $userId")


        binding.circleBtn.setOnClickListener {
            onCircleButtonClick.invoke()
            popupWindow.dismiss()
        }
    }

    fun getPopupData(binding: ItemMappopupBinding, userId: Int, latitude: String, longitude: String) {
        val apiService = RetrofitClient.instance.create(PopupInterface::class.java)
        val accessToken = AccessTokenManager.getAccessToken() ?: ""

        apiService.getPopupData(accessToken,"application/json",userId,latitude,longitude).enqueue(object : Callback<PopupResponse> {
            override fun onResponse(call: Call<PopupResponse>, response: Response<PopupResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.memory?.let { memory ->
                        binding.tvPopupTitle.text = memory.title
                        binding.tvPopupDate.text = "방문일: ${memory.visit_date} / 친구: ${memory.friends}"
                        binding.tvPopupDescription.text = memory.gpt_summary
                        Glide.with(binding.imgPopup.context)
                            .load(RetrofitClient.BASE_URL + memory.image_url)
                            .into(binding.imgPopup)
                    }
                } else {
                    binding.tvPopupTitle.text = "데이터를 불러오는 중."
                    binding.tvPopupDate.text = "데이터를 불러오는 중."
                    binding.tvPopupDescription.text = "데이터를 불러오는 중."
                }
            }

            override fun onFailure(call: Call<PopupResponse>, t: Throwable) {
                binding.tvPopupTitle.text = "데이터를 불러올 수 없습니다."
                binding.tvPopupDate.text = "데이터를 불러올 수 없습니다."
                binding.tvPopupDescription.text = "데이터를 불러올 수 없습니다."
            }
        })
    }

    private fun getMapLocation(): Pair<String, String> {
        val spfLocation = context.getSharedPreferences("map_location", Context.MODE_PRIVATE)

        // 저장된 값들을 가져옵니다. 기본값은 "0.0"으로 설정합니다.
        val latitude = spfLocation.getString("latitude", "")
        val longitude = spfLocation.getString("longitude", "")
        Log.d("PopupActivity", "위도: $latitude, 경도: $longitude")
        return Pair("latitude", "longitude")
    }

}

