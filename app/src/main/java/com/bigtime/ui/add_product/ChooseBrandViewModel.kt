package com.bigtime.ui.add_product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bigtime.common.AbsentLiveData
import com.bigtime.data.api.BaseResponse
import com.bigtime.data.api.Resource
import com.bigtime.data.model.Brands
import com.bigtime.repo.UMSRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * Created by Tony Augustine on 17,August,2019
 * tonyaugustine47@gmail.com
 */
class ChooseBrandViewModel
@Inject constructor(repo: UMSRepository) : ViewModel(){


    private val apiParams = MutableLiveData<HashMap<String, String>>()


    fun loadBrands(map: HashMap<String, String>) {
        apiParams.value = map
//        val jsonObject = JsonObject()
//        val gson = Gson()
//        val text: String = gson.toJson(jsonObject)
//        val requestBody = RequestBody.create(MediaType.parse("text/plain"), text)
    }

    val getBrands: LiveData<Resource<BaseResponse<Brands>>> =
            Transformations.switchMap(apiParams) { data ->
                if (data == null){
                    AbsentLiveData.create()
                }else{
                    repo.loadBrands(data)
                }
            }
}