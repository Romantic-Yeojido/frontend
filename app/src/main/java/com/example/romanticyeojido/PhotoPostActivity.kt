package com.example.romanticyeojido

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.romanticyeojido.network.Photo
import com.example.romanticyeojido.network.PhotoInterface
import com.example.romanticyeojido.network.convertUriToMultipartBody
import com.example.romanticyeojido.network.RetrofitClient
import com.example.romanticyeojido.ui.locker.PhotoPagerAdapter
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class PhotoPostActivity : AppCompatActivity() {
    private lateinit var adapter: PhotoPagerAdapter // ViewPager2 Adapter
    private lateinit var viewPager2: ViewPager2 // ViewPager2 연결

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_photo_pager)

        // ViewPager2 초기화
        viewPager2 = findViewById(R.id.viewPager2)

        // 예시: 사진 목록을 가져온 후 UI에 표시
        val memoryId = 123 // 실제 메모리 ID로 변경
        getPhotosFromServer(memoryId)

        // 등록 버튼 클릭 리스너 설정
        val postRegisterButton = findViewById<TextView>(R.id.post_register_btn)
        postRegisterButton.setOnClickListener {
            val selectedImageUri = Uri.parse("file:///path/to/your/image.jpg") // 실제 이미지 URI로 변경
            postPhotoToServer(selectedImageUri, memoryId)
        }
    }

    // POST 요청 (사진 업로드)
    private fun postPhotoToServer(imageUri: Uri, memoryId: Int) {
        val retrofit = RetrofitClient.instance
        val photoPostInterface = retrofit.create(PhotoInterface::class.java)

        val token = "Bearer your_token" // Authorization 헤더에 추가할 토큰
        val multipartImage = convertUriToMultipartBody(imageUri, contentResolver)

        photoPostInterface.postUserPhotos(token, memoryId, multipartImage!!)
            .enqueue(object : Callback<List<Photo>> {
                override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                    if (response.isSuccessful) {
                        // 서버 응답 성공 처리
                        val photoList = response.body()
                        Log.d("PhotoUpload", "Success: ${photoList?.size}")
                    } else {
                        // 서버 응답 실패 처리
                        Log.e("PhotoUpload", "Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                    Log.e("PhotoUpload", "Failure: ${t.message}")
                }
            })
    }

    // GET 요청 (사진 목록 가져오기)
    private fun getPhotosFromServer(memoryId: Int) {
        val retrofit = RetrofitClient.instance
        val photoPostInterface = retrofit.create(PhotoInterface::class.java)

        val token = "Bearer your_token" // Authorization 헤더에 추가할 토큰

        photoPostInterface.getUserPhotos(token, memoryId.toString())
            .enqueue(object : Callback<List<Photo>> {
                override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                    if (response.isSuccessful) {
                        val photos = response.body()
                        photos?.let {
                            // ViewPager2에 데이터 표시
                            adapter = PhotoPagerAdapter(it)
                            viewPager2.adapter = adapter
                        }
                    } else {
                        Log.e("PhotoList", "Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                    Log.e("PhotoList", "Failure: ${t.message}")
                }
            })
    }
}

