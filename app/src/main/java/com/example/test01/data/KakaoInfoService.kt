package com.example.test01.data

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface KakaoInfoService {

    @FormUrlEncoded
    @POST("")
    fun requestKakaoLogin(
        @Field("Authorization") Authorization:String
    ) : Call<KakaoInfo>

}