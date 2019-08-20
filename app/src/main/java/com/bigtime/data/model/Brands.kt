package com.bigtime.data.model

data class Brands(
	val mainCategory: List<MainCategoryItem?>? = null,
	val brandList: List<BrandListItem?>? = null,
	val message: String? = null,
	val status: Int? = null
)
