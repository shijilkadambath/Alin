package com.bigtime.data.model

import com.google.gson.annotations.SerializedName

data class Product(


        @SerializedName("productID") val productID : Int,
        @SerializedName("brandName") val brandName : String,
        @SerializedName("brandNameId") val brandNameId : Int,
        @SerializedName("brandCounter") val brandCounter : Int,
        @SerializedName("title") val title : String,
        @SerializedName("mrp") val mrp : Int,
        @SerializedName("skPrice") val skPrice : Int,
        @SerializedName("mainCategory") val mainCategory : String,
        @SerializedName("subCategory") val subCategory : String,
        @SerializedName("variants") val variants : List<ProductVariant>


)
