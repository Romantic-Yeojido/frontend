package com.example.romanticyeojido.ui.map

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.example.romanticyeojido.R
import com.example.romanticyeojido.databinding.ItemMappopupBinding
import com.example.romanticyeojido.network.socialLogin.AccessTokenManager
import com.example.romanticyeojido.network.map.PopupInterface
//import com.example.romanticyeojido.network.BASE_URL
import com.example.romanticyeojido.network.map.PopupResponse
import com.example.romanticyeojido.network.RetrofitClient
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

        apiService.getPopupData(userId,latitude,longitude).enqueue(object : Callback<PopupResponse> {
            override fun onResponse(call: Call<PopupResponse>, response: Response<PopupResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.result?.let { result ->
                    // 성공 로그 추가
                        Log.d("PopupData", "데이터를 성공적으로 불러왔습니다: ${result.title}, 방문일: ${result.visit_date}, 친구: ${result.friends}")

                        binding.tvPopupTitle.text = result.title
                        binding.tvPopupDate.text = "방문일: ${result.visit_date} / 친구: ${result.friends}"
                        binding.tvPopupDescription.text = result.gpt_summary
                        Glide.with(binding.imgPopup.context)
                            .load(RetrofitClient.BASE_URL + result.image_url)
                            .into(binding.imgPopup)
                    }
                } else {
                    // 응답이 성공적이지 않은 경우에 대한 로그 추가
                    if (response.body() == null) {
                        Log.e("PopupData", "서버 응답 본문이 null입니다. 응답 코드: ${response.code()}")
                    } else {
                        Log.e("PopupData", "서버에서 응답했지만 실패했습니다. 응답 코드: ${response.code()}, 성공 여부: ${response.body()?.success}")
                    }

                    // 사용자에게 표시할 메시지
                    binding.tvPopupTitle.text = "데이터를 불러오는 중."
                    binding.tvPopupDate.text = "데이터를 불러오는 중."
                    binding.tvPopupDescription.text = "데이터를 불러오는 중."
                }
            }

            override fun onFailure(call: Call<PopupResponse>, t: Throwable) {
                // API 호출 실패에 대한 로그 추가
                Log.e("PopupData", "API 호출 중 오류 발생: ${t.message}")

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

