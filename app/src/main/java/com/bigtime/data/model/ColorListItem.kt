package com.bigtime.data.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class ColorListItem(
        @field:SerializedName("Color Hex")
        val colorHex: String? = null,
        val ID: Int? = null,
        @field:SerializedName("Color Name")
        val colorName: String? = null
)
