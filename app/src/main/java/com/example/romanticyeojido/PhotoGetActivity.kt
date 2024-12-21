package com.example.romanticyeojido

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.romanticyeojido.network.locker.Photo
import com.example.romanticyeojido.network.locker.PhotoInterface
import com.example.romanticyeojido.network.RetrofitClient
import com.example.romanticyeojido.ui.locker.PhotoPagerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoGetActivity : AppCompatActivity() {

    private lateinit var photoAdapter: PhotoPagerAdapter
    private lateinit var viewPager2: ViewPager2

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        viewPager2 = findViewById(R.id.viewPager2)

        // 예시로 memoryId를 1로 설정 (게시물 ID에 해당)
        val memoryId = 1
        getPhotosFromServer(memoryId)
    }

    // GET 요청 (사진 목록 가져오기)
    private fun getPhotosFromServer(memoryId: Int) {
        val retrofit = RetrofitClient.instance
        val photoPostInterface = retrofit.create(PhotoInterface::class.java)

        val token = "Bearer your_token" // Authorization 헤더에 추가할 토큰

        photoPostInterface.getUserPhotos(memoryId.toString(), token)
            .enqueue(object : Callback<List<Photo>> {
                override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                    if (response.isSuccessful) {
                        // 서버 응답 성공 처리
                        val photos = response.body()
                        photos?.let {
                            // 받은 사진 목록을 RecyclerView에 표시
                            photoAdapter = PhotoPagerAdapter(it)
                            viewPager2.adapter = photoAdapter
                            Log.d("PhotoList", "Success: ${it.size} photos retrieved")
                        }
                    } else {
                        // 서버 응답 실패 처리
                        Log.e("PhotoList", "Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                    // 실패 처리
                    Log.e("PhotoList", "Failure: ${t.message}")
                }
            })
    }
}
