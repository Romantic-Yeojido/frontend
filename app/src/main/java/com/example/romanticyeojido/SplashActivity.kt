package com.example.romanticyeojido

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.romanticyeojido.databinding.ActivityMainBinding
import com.example.romanticyeojido.databinding.ActivitySplashBinding
import com.example.romanticyeojido.network.AuthResponse
import com.example.romanticyeojido.network.NaverAuthInterface
import com.example.romanticyeojido.network.RetrofitClient
import com.example.romanticyeojido.network.UserInfoDialog
import com.example.romanticyeojido.network.Utility
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import retrofit2.Call
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val TAG = this.javaClass.simpleName

    private var userId: String = ""
    private var email: String = ""
    private var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }, 2000)

        val keyHash = Utility.getKeyHash(this)
        Log.d("KeyHash", keyHash ?: "Key hash not found")

        binding.kakaoLoginBtn.setOnClickListener {
            kakaoLogin()
        }

        KakaoSdk.init(this, BuildConfig.NATIVE_APP_KEY)
        NaverLoginLogic()
   }

    private fun NaverLoginLogic() {
        binding.run {
            naverLoginBtn.setOnClickListener {
                val oAuthLoginCallback = object : OAuthLoginCallback {
                    override fun onSuccess() {
                        // 네이버 로그인 API 호출 성공 시 유저 정보를 가져온다
                        NidOAuthLogin().callProfileApi(object :
                            NidProfileCallback<NidProfileResponse> {
                            override fun onSuccess(result: NidProfileResponse) {
                                userId = result.profile?.id.toString() //네이버에서 제공하는 고유 id
                                name = result.profile?.name.toString()
                                email = result.profile?.email.toString()


                                val accessToken = NaverIdLoginSDK.getAccessToken()
                                Log.d(TAG, "accessToken: $accessToken")

//                                if (!accessToken.isNullOrEmpty()) {
//                                    fetchAuthUrlFormServer(accessToken)
//                                } else {
//                                    Log.e(TAG, "AccessToken is null or empty")
//                                }

                                Log.e(TAG, "네이버 로그인한 유저 정보 - User ID : $userId")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 이름 : $name")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 이메일 : $email")
                                Log.d(TAG, "Context: ${this@SplashActivity}")
                                Log.d(TAG, "Client ID: ${BuildConfig.NAVER_CLIENT_ID}")
                                Log.d(TAG, "Client Secret: ${BuildConfig.NAVER_CLIENT_SECRET}")

                                // MainActivity로 유저 정보를 전달
                                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                                intent.putExtra("USER_ID", userId)
                                intent.putExtra("USER_NAME", name)
                                intent.putExtra("USER_EMAIL", email)
                                startActivity(intent)
                                finish()
                            }

                            override fun onError(errorCode: Int, message: String) {
                                Log.e(TAG, "Naver Profile API Error: $message")
                            }

                            override fun onFailure(httpStatus: Int, message: String) {
                                Log.e(TAG, "Naver Profile API Failure: $message")
                            }
                        })
                    }

                    override fun onError(errorCode: Int, message: String) {
                        val naverAccessToken = NaverIdLoginSDK.getAccessToken()
                        Log.e(TAG, "naverAccessToken : $naverAccessToken")
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        Log.e(TAG, "Naver Login Failure: $message")
                    }
                }

                NaverIdLoginSDK.initialize(
                    this@SplashActivity,
                    BuildConfig.NAVER_CLIENT_ID,
                    BuildConfig.NAVER_CLIENT_SECRET,
                    "낭만여지도"
                )
                NaverIdLoginSDK.authenticate(this@SplashActivity, oAuthLoginCallback)
            }
        }
    }

//    private fun fetchAuthUrlFormServer(accessToken: String) {
//        Log.d(TAG, "fetchAuthUrlFormServer called with accessToken: $accessToken")
//        val naverApiService = RetrofitClient.instance.create(NaverAuthInterface::class.java)
//
//        naverApiService.getAuthUrl(
//            token = "Bearer $accessToken"
//        ).enqueue(object : retrofit2.Callback<AuthResponse>{
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//
//                Log.d(TAG, "Response Code: ${response.code()}")
//
//                if (response.isSuccessful) {
//                    val authResponse = response.body()
//                    authResponse?.let {
//                        Log.d(TAG, "Auth URL: ${it.auth_url}")
//                        Log.d(TAG, "State: ${it.state}")
//
//                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.auth_url))
//                        startActivity(intent)
//                    }
//                } else {
//                    Log.e(TAG, "Server Error: ${response.code()}")
//                    // errorBody()가 null일 수 있으므로 이를 먼저 확인
//                    val errorBody = response.errorBody()?.string()
//                    if (errorBody.isNullOrEmpty()) {
//                        Log.e(TAG, "Response Body is empty or null")
//                    } else {
//                        Log.e(TAG, "Response Body: $errorBody")
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                Log.e(TAG, "Network Faliure: ${t.message}")
//            }
//        })
//
//        intent.putExtra("API_SUCCESS", true)
//    }

    private fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                Log.d("token", token.accessToken)
                val spf = getSharedPreferences("auth3", MODE_PRIVATE)
                val editor = spf.edit()
                editor.putString("kakao_token", token.accessToken)
                editor.apply()

                getKakaoUserInfo()
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            // 이메일 권한을 요청하면서 로그인
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                if (error != null) {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                } else if (token != null) {
                    // 이메일 권한이 이미 동의된 경우 정보 가져오기
                    getKakaoUserInfo()
                }
            }
        }
    }

    private fun getKakaoUserInfo() {
        UserApiClient.instance.me { kakaoUser, error ->
            if (error != null) {
                Toast.makeText(this, "사용자 정보 요청 실패", Toast.LENGTH_SHORT).show()
            } else if (kakaoUser != null) {
                // 사용자 정보를 가져왔으므로 팝업창을 띄웁니다
                showUserInfoPopup(kakaoUser)
            }
        }
    }

    private fun showUserInfoPopup(kakaoUser: User) {
        val nickname = kakaoUser.kakaoAccount?.profile?.nickname
        val profileImageUrl = kakaoUser.kakaoAccount?.profile?.profileImageUrl
        val email = kakaoUser.kakaoAccount?.email

        // 사용자 정보를 팝업창으로 띄우기
        val dialog = UserInfoDialog(this, nickname, profileImageUrl, email)
        dialog.setOnConfirmListener {
            // CONFIRM 버튼 클릭 시 메인 액티비티로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()  // 현재 액티비티 종료
        }
        dialog.show()
    }
}