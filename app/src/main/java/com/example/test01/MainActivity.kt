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

        val btnKakaoLogin = findViewById<ImageButton>(R.id.btn_login2) // 로그인 버튼

        btnKakaoLogin.setOnClickListener {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                    Toast.makeText(this, "실패".plus(error.toString()), Toast.LENGTH_SHORT).show()
                }
                else if (token != null) {
                    Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
