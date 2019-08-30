package com.bigtime.data.api

/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */

import com.google.gson.annotations.SerializedName

/**
 * Base Gson class structure of all api responses.
 */

data class BaseResponseThree<T,R,S>(

        @field:SerializedName("function")
        val function: String,

        @field:SerializedName("status")
        val status: Int,

        @field:SerializedName("message")
        val message: String,

        @field:SerializedName("status_code")
        val statusCode: Int,

        @SerializedName(value="data", alternate=(arrayOf("category")))
        val data: T?,

        @SerializedName(value="data2", alternate=(arrayOf("soles")))
        val data2: R,

        @SerializedName(value = "data3", alternate = (arrayOf("footwearType")))
        val data3: S?

) {
    fun isSuccess(): Boolean {
        return status == 1
    }
}