/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bigtime.data.api

import androidx.lifecycle.LiveData
import com.bigtime.data.model.*
import com.bigtime.data.model.product_details.CategoryItem
import com.bigtime.data.model.product_details.FootwearTypeItem
import com.bigtime.data.model.product_details.ProductPreview
import com.bigtime.data.model.product_details.SolesItem
import com.bigtime.utils.SessionUtils
import com.google.gson.JsonObject
import retrofit2.http.*

/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */


/**
 * REST API access points
 */
interface WebService {


   /* @GET("search/repositories")
    fun searchRepos(@Query("q") query: String): LiveData<ApiResponse<User>>*/


    @GET("api/speciality_list")
    fun loadUsers(): LiveData<ApiResponse<BaseResponse<List<User>>>>

    @POST("api/sellerApp/data/getBrandData/")
    fun loadBrands(@HeaderMap params: Map<String, String>, @Body body: JsonObject): LiveData<ApiResponse<BaseResponseTwo<ArrayList<Brand>,ArrayList<MainCategory>>>>


    @POST("api/sellerApp/data/productPreview/")
    fun productPreview(@HeaderMap params: Map<String, String>, @Body body: JsonObject): LiveData<ApiResponse<BaseResponse<ProductPreview>>>




    @POST("userservice/sellerLogin/")
    fun login(@HeaderMap params: Map<String, String>, @Body body: Map<String,String>): LiveData<ApiResponse<BaseResponse<SessionUtils.LoginSession>>>


    @POST("userservice/changePassword/")
    fun changePassword(@HeaderMap params: Map<String, String>, @Body body: Map<String,String>): LiveData<ApiResponse<BaseResponse<Any>>>


    @POST("userservice/forgotPassword/")
    fun forgotPassword(@HeaderMap params: Map<String, String>, @Body body: Map<String,String>): LiveData<ApiResponse<BaseResponse<String>>>

    @POST("api/sellerApp/data/getProductFormDetails/")
    fun loadBrandDetails(@HeaderMap params: Map<String, String>, @Body body: Map<String, String>): LiveData<ApiResponse<BaseResponseThree<ArrayList<CategoryItem>, ArrayList<SolesItem>, ArrayList<FootwearTypeItem>>>>


    @POST("/orders/globalCount/")
    fun loadOrderDetails(@HeaderMap params: Map<String, String>, @Body body: JsonObject): LiveData<ApiResponse<BaseResponse<ArrayList<Order>>>>

    @POST("api/sellerApp/data/approvedProducts/")
    fun loadApprovedProducts(@HeaderMap params: Map<String, String>, @Body body: JsonObject): LiveData<ApiResponse<BaseResponse<ArrayList<Product>>>>

    @POST("api/sellerApp/data/getColorList/")
    fun loadColors(@HeaderMap params: Map<String, String>, @Body body: Map<String, String>): LiveData<ApiResponse<ColorData>>

    @FormUrlEncoded
    @POST("api/user/general/uploadImage/")
    fun uploadImage(@HeaderMap params: Map<String, String>, @FieldMap body: Map<String, String>): LiveData<ApiResponse<DataImageUpload>>


}
