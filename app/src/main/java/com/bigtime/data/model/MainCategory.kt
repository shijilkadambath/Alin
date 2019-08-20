package com.bigtime.data.model

import com.google.gson.annotations.SerializedName

data class MainCategory(
		@field:SerializedName("Category")
	val category: String? = null,
		@field:SerializedName("ID")
	val iD: Int? = null
)
