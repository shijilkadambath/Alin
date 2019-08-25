package com.bigtime.data.api

/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */

import com.google.gson.annotations.SerializedName

/**
 * Base Gson class structure of all api responses.
 */

data class BaseResponseTwo<T,R>(

        @field:SerializedName("function")
        val function: String,

        @field:SerializedName("status")
        val status: Int,

        @field:SerializedName("message")
        val message: String,

        @field:SerializedName("status_code")
        val statusCode: Int,

        @SerializedName(value="data", alternate=(arrayOf("brandList")))
        val data: T?,

        @SerializedName(value="data2", alternate=(arrayOf("mainCategory")))
        val data2: R?

) {
    fun isSuccess(): Boolean {
        return status == 1
    }
}