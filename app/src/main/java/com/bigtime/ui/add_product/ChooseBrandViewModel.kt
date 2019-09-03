package com.bigtime.ui.add_product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bigtime.common.AbsentLiveData
import com.bigtime.data.api.BaseResponse
import com.bigtime.data.api.BaseResponseTwo
import com.bigtime.data.api.Resource
import com.bigtime.data.model.Brand
import com.bigtime.data.model.MainCategory
import com.bigtime.repo.UMSRepository
import javax.inject.Inject

/**
 * Created by Tony Augustine on 17,August,2019
 * tonyaugustine47@gmail.com
 */
class ChooseBrandViewModel
@Inject constructor(repo: UMSRepository) : ViewModel(){


    private val apiParams = MutableLiveData<String>()

    private var selectedBrand: Brand? = null

    fun loadBrands() {
        apiParams.value = "test"
//        val jsonObject = JsonObject()
//        val gson = Gson()
//        val text: String = gson.toJson(jsonObject)
//        val requestBody = RequestBody.create(MediaType.parse("text/plain"), text)
    }

    val getBrands: LiveData<Resource<BaseResponseTwo<ArrayList<Brand>, ArrayList<MainCategory>>>> =
            Transformations.switchMap(apiParams) { data ->
                if (data == null){
                    AbsentLiveData.create()
                }else{
                    repo.loadBrands()
                }
            }

    fun selectedItem(brand: Brand?) {
        this.selectedBrand = brand
    }

    fun getSelectedBrand(): Brand? {
        return selectedBrand
    }
 }