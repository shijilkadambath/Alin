package com.bigtime.data.model

/**
 * Created by Tony Augustine on 02,September,2019
 * tonyaugustine47@gmail.com
 */
data class DataImage(var imagePath : String,
                     var colorList: ArrayList<ColorListItem?>) {
    var selectedColor: String? = null
    var selectedArticle: String? = null
}