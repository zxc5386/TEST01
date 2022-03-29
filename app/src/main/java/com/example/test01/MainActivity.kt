package com.example.test01

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.example.test01.data.KakaoInfo
import com.example.test01.data.KakaoInfoService
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*카카오 해쉬 키*/
        //var keyHash = Utility.getKeyHash(this)
        //println("keyHash : $keyHash")

        val btnKakaoLogin = findViewById<ImageButton>(R.id.btn_login2) // 카카오 로그인 버튼

        btnKakaoLogin.setOnClickListener {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                    Toast.makeText(this, "연결실패 : ".plus(error.toString()), Toast.LENGTH_SHORT).show()
                }
                else if (token != null) {
                    Log.d(TAG, "로그인 성공 \n ${token.accessToken}")
                    Log.d(TAG, "로그인 성공 \n ${token.toString()}")
                    Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()

                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://kapi.kakao.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val loginService: KakaoInfoService = retrofit.create(KakaoInfoService::class.java)

                    loginService.requestKakaoLogin("Bearer ${token.accessToken}").enqueue(object:
                        Callback<KakaoInfo> {
                        override fun onFailure(call: Call<KakaoInfo>, t: Throwable) {
                            //실패할 경우
                            Log.d(TAG, "2차 로그인 실패 \n ${t.message}")
                            Toast.makeText(this@MainActivity, "실패", Toast.LENGTH_SHORT).show()
                        }

                        override fun onResponse(call: Call<KakaoInfo>, response: Response<KakaoInfo>) {
                            //정상응답이 올경우
                            Log.d(TAG, "2차 로그인 성공 response : \n ${response.toString()}")
                            Log.d(TAG, "2차 로그인 성공 response.body : \n ${response.body().toString()}")
                            Toast.makeText(this@MainActivity, "성공", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }
}
