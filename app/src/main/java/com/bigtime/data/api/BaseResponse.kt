package com.bigtime.data.api

/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */

import com.google.gson.annotations.SerializedName

/**
 * Base Gson class structure of all api responses.
 */

data class BaseResponse<T>(

        @field:SerializedName("function")
        val function: String,

        @field:SerializedName("status")
        val status: Int,

        @field:SerializedName("message")
        val message: String,

        @field:SerializedName("status_code")
        val statusCode: Int,

        @SerializedName(value="skAuth_Token", alternate=(arrayOf("accessToken")))
        val token: String,

        @field:SerializedName(value="data", alternate=(arrayOf("otp")))
        val data: T?

) {
    fun isSuccess(): Boolean {
        return status == 1
    }
}