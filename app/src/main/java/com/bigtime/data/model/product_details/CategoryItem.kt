package com.bigtime.data.model.product_details

data class CategoryItem(
	val subCategory: List<SubCategoryItem?>? = null,
	val category: String? = null,
	val iD: Int? = null
)
