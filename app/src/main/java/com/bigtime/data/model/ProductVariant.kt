package com.bigtime.data.model

import com.google.gson.annotations.SerializedName

data class ProductVariant(


        @SerializedName("variantID") val variantID : Int,
        @SerializedName("colorID") val colorID : Int,
        @SerializedName("defImage") val defImage : String,
        @SerializedName("brandCode") val brandCode : String,
        @SerializedName("isActive") val isActive : Int


)
