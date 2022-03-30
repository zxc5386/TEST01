package com.example.test01

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.oauth.view.NidOAuthLoginButton
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfile
import com.navercorp.nid.profile.data.NidProfileResponse

class MainActivity : AppCompatActivity() {

    final val RC_SIGN_IN = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*카카오 해쉬 키*/
        //var keyHash = Utility.getKeyHash(this)
        //println("keyHash : $keyHash")

        /*카카오로그인*/
        /*************************************************************************************************/
        val btnKakaoLogin = findViewById<ImageButton>(R.id.btn_login2) // 카카오 로그인 버튼

        btnKakaoLogin.setOnClickListener {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                    Toast.makeText(this, "연결실패 : ".plus(error.toString()), Toast.LENGTH_SHORT).show()
                }
                else if (token != null) {
                    Log.d(TAG, "로그인 성공 \n ${token.toString()}")
                    // 사용자 정보 요청 (기본)
                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e(TAG, "사용자 정보 요청 실패", error)
                            Toast.makeText(this, "사용자 정보 요청 실패", Toast.LENGTH_SHORT).show()
                        }
                        else if (user != null) {
                            Log.i(TAG, "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                            Toast.makeText(this, "이메일 : ${user.kakaoAccount?.email}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        /*************************************************************************************************/


        /*구글로그인*/
        /*************************************************************************************************/
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val btnGoogleLogin = findViewById<SignInButton>(R.id.sign_in_button) // 구글 로그인 버튼

        btnGoogleLogin.setOnClickListener {
            //Toast.makeText(this, "구글 : ", Toast.LENGTH_SHORT).show()
            signIn()
        }
        /*************************************************************************************************/

        /*naver로그인*/
        /*************************************************************************************************/
        NaverIdLoginSDK.initialize(this, "ezCPnyIfSIN816GBKFRb", "IJJu9E5JRn","test001")

        val btnNaverLogin = findViewById<NidOAuthLoginButton>(R.id.buttonOAuthLoginImg) // 카카오 로그인 버튼

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                val tvAccessToken = NaverIdLoginSDK.getAccessToken()
                val tvRefreshToken = NaverIdLoginSDK.getRefreshToken()
                val tvExpires = NaverIdLoginSDK.getExpiresAt().toString()
                val tvType = NaverIdLoginSDK.getTokenType()
                val tvState = NaverIdLoginSDK.getState().toString()

                Log.d("tvAccessToken",tvAccessToken.toString())
                Log.d("tvRefreshToken",tvRefreshToken.toString())
                Log.d("tvExpires",tvExpires.toString())
                Log.d("tvType",tvType.toString())
                Log.d("tvState",tvState.toString())

                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                    override fun onSuccess(response: NidProfileResponse) {
                        Log.d("로그인성공 :", "$response")
                        val naverApi : NidProfile? = response.profile
                        val nEmail = naverApi!!.email.toString()
                        Log.d("email :", nEmail)
                        Toast.makeText(this@MainActivity,nEmail ,Toast.LENGTH_SHORT).show()
                    }
                    override fun onFailure(httpStatus: Int, message: String) {
                        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                        Log.d("로그인성공에러 :", "$errorCode , $errorDescription")
                        Toast.makeText(this@MainActivity, "errorCode: $errorCode, errorDesc: $errorDescription", Toast.LENGTH_SHORT).show()
                    }
                    override fun onError(errorCode: Int, message: String) {
                        onFailure(errorCode, message)
                    }
                })
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@MainActivity,"errorCode:$errorCode, errorDesc:$errorDescription",Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        btnNaverLogin.setOnClickListener {
            //Toast.makeText(this, "NAVER", Toast.LENGTH_SHORT).show()
            NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
        }
        /*************************************************************************************************/
    }

    /*구글로그인*/
    /*************************************************************************************************/
    private fun signIn() {
        var signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val email = account?.email.toString()
            val familyName = account?.familyName.toString()
            val givenName = account?.givenName.toString()
            val displayName = account?.displayName.toString()

            Log.d("account", email)
            Log.d("account", familyName)
            Log.d("account", givenName)
            Log.d("account", displayName)

            Toast.makeText(this, "이메일 : $email", Toast.LENGTH_SHORT).show()

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this, "failed : $e.statusCode", Toast.LENGTH_SHORT).show()
        }
    }
    /*************************************************************************************************/

}