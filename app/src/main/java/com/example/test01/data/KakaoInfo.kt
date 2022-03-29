package com.example.test01.data

import com.google.gson.annotations.SerializedName

data class KakaoInfo(
    @SerializedName("secure_resource")
    val secure_resource: Boolean,

    @SerializedName("property_keys")
    val property_keys: String
)
