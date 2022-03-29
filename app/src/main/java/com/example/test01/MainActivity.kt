package com.example.test01

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.kakao.sdk.user.UserApiClient

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

                    // 사용자 정보 요청 (기본)
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                        }
                        else if (user != null) {
                            Log.i(TAG, "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                        }
                    }
                }
            }
        }
    }
}