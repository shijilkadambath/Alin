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

package com.bigtime.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.bigtime.AppExecutors
import com.bigtime.data.api.*
import com.bigtime.data.db.AppDb
import com.bigtime.data.db.UMSDao
import com.bigtime.data.model.*
import com.bigtime.data.model.product_details.CategoryItem
import com.bigtime.data.model.product_details.FootwearTypeItem
import com.bigtime.data.model.product_details.ProductPreview
import com.bigtime.data.model.product_details.SolesItem
import com.bigtime.utils.AppConstants
import com.bigtime.utils.SessionUtils
import com.google.gson.JsonObject
import org.json.JSONObject
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Shijil Kadambath on 03/08/2018
Email : shijilkadambath@gmail.com
 */

/**
 * Repository that handles User instances.
 *
 */
@Singleton
class UMSRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val db: AppDb,
        private val umsoDao: UMSDao,
        private val webService: WebService
) {
    //ApiResponse<BaseResponse<List<User>>>




    fun login(data: Map<String, String>): LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> {

        val header = HashMap<String, String>()
        header["SOURCE"] = "cCX1G9EVpL"
        header["PLATFORM"] = "Android"
        header["PACKAGE-NAME"] = "com.bizcrum.shoekonnect"
        header["SKAUTH-TOKEN"] = "0"
        header["Content-Type"] = "application/json"

        AppConstants.HOST = AppConstants.HOST_LOGIN
        AppConstants.PORT = 4000
        return object : NetworkBoundResourceNoCache<BaseResponse<SessionUtils.LoginSession>>(appExecutors) {

            override fun createCall(): LiveData<ApiResponse<BaseResponse<SessionUtils.LoginSession>>> {
                return webService.login(header, data)
            }
        }.asLiveData()


    }

    fun forgotPassword(data: HashMap<String, String>): LiveData<Resource<BaseResponse<String>>> {
        val header = HashMap<String, String>()
        header["SOURCE"] = "cCX1G9EVpL"
        header["PLATFORM"] = "Android"
        header["PACKAGE-NAME"] = "com.bizcrum.shoekonnect"
        header["SKAUTH-TOKEN"] = "0"
        header["Content-Type"] = "application/json"

        AppConstants.HOST = AppConstants.HOST_LOGIN
        AppConstants.PORT = 4000
        return object : NetworkBoundResourceNoCache<BaseResponse<String>>(appExecutors) {

            override fun createCall(): LiveData<ApiResponse<BaseResponse<String>>> {
                return webService.forgotPassword(header, data)
            }
        }.asLiveData()
    }

    fun verifyOTP(data: HashMap<String, String>): LiveData<Resource<BaseResponse<Any>>> {

        val header = HashMap<String, String>()
        header["SOURCE"] = "cCX1G9EVpL"
        header["PLATFORM"] = "Android"
        header["PACKAGE-NAME"] = "com.bizcrum.shoekonnect"
        header["SKAUTH-TOKEN"] = data["token"]!!
        header["Content-Type"] = "application/json"

        AppConstants.HOST = AppConstants.HOST_LOGIN
        AppConstants.PORT = 4000
        return object : NetworkBoundResourceNoCache<BaseResponse<Any>>(appExecutors) {

            override fun createCall(): LiveData<ApiResponse<BaseResponse<Any>>> {
                return webService.changePassword(header, data)
            }
        }.asLiveData()
    }


    fun resetPassword(data: HashMap<String, String>): LiveData<Resource<BaseResponse<Any>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun forgotPasswordValidateOTP(data: HashMap<String, String>): LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun verifyPhone(data: HashMap<String, String>): LiveData<Resource<BaseResponse<Any>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun registration(data: HashMap<String, String>): LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun sentOtp(data: HashMap<String, String>): LiveData<Resource<BaseResponse<Any>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    fun loadBrands(data: HashMap<String, String>): LiveData<Resource<BaseResponseTwo<ArrayList<Brand>, ArrayList<MainCategory>>>> {
        AppConstants.HOST = AppConstants.HOST_DEV4
        AppConstants.PORT = 80

        return object : NetworkBoundResourceNoCache<BaseResponseTwo<ArrayList<Brand>, ArrayList<MainCategory>>>(appExecutors) {

            override fun createCall(): LiveData<ApiResponse<BaseResponseTwo<ArrayList<Brand>, ArrayList<MainCategory>>>> {
                return webService.loadBrands(data, JsonObject().apply {
                    addProperty("a", "b")
                })
            }
        }.asLiveData()
    }

    fun loadBrandDetails(data: HashMap<String, String>): LiveData<Resource<BaseResponseThree<ArrayList<CategoryItem>, ArrayList<SolesItem>, ArrayList<FootwearTypeItem>>>> {

        AppConstants.HOST = AppConstants.HOST_DEV4
        AppConstants.PORT = 80
        val header = HashMap<String, String>()
        header["sessionToken"] = "IlNLMTQ5MDI3MTA2NDE1NjYwNTUwNjUi:1hz0Se:SpzvNCcH2GsDdJSVukbZhZiJb3U"
        header["platform"] = "postman"
        header["packageName"] = "com.bizcrum.shoekonnect"
        header["isAuthRequired"] = "true"
        header["Content-Type"] = "application/json"



        return object : NetworkBoundResourceNoCache<BaseResponseThree<ArrayList<CategoryItem>, ArrayList<SolesItem>, ArrayList<FootwearTypeItem>>>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<BaseResponseThree<ArrayList<CategoryItem>, ArrayList<SolesItem>, ArrayList<FootwearTypeItem>>>> {
                return webService.loadBrandDetails(header, data)
            }

        }.asLiveData()
    }
    fun loadProductPreview(productID: String): LiveData<Resource<BaseResponse<ProductPreview>>> {
        AppConstants.HOST = AppConstants.HOST_DEV4
        AppConstants.PORT = 80

        val header = HashMap<String, String>()
        header["platform"] = "Android"
        header["isAuthRequired"] = "true"
        header["packageName"] = "com.bizcrum.shoekonnect"
        header["sessionToken"] = SessionUtils.getAuthTokens(true)!!
        header["Content-Type"] = "application/json"

        return object : NetworkBoundResourceNoCache<BaseResponse<ProductPreview>>(appExecutors) {

            override fun createCall(): LiveData<ApiResponse<BaseResponse<ProductPreview>>> {
                return webService.productPreview(header, JsonObject().apply {
                    addProperty("productID", productID)
                })
            }
        }.asLiveData()
    }

    fun loadOrderDetais(data: HashMap<String, String>): LiveData<Resource<BaseResponse<ArrayList<Order>>>> {
        AppConstants.HOST = AppConstants.HOST_LOGIN
        AppConstants.PORT = 4000
        val header = HashMap<String, String>()
        header["SOURCE"] = "cCX1G9EVpL"
        header["PLATFORM"] = "Android"
        header["PACKAGE-NAME"] = "com.bizcrum.shoekonnect"
        header["SKAUTH-TOKEN"] = SessionUtils.getAuthTokens(true)!!
        header["Content-Type"] = "application/json"


        return object : NetworkBoundResourceNoCache<BaseResponse<ArrayList<Order>>>(appExecutors) {

            override fun createCall(): LiveData<ApiResponse<BaseResponse<ArrayList<Order>>>> {
                return webService.loadOrderDetails(header, JsonObject().apply {
                    addProperty("userID", data["userID"]!!)
                })
            }
        }.asLiveData()
    }

    fun loadApprovedProducts(data: HashMap<String, String>): LiveData<Resource<BaseResponse<ArrayList<Product>>>> {
        AppConstants.HOST = AppConstants.HOST_DEV4
        AppConstants.PORT = 80
        val header = HashMap<String, String>()
        header["platform"] = "Android"
        header["isAuthRequired"] = "true"
        header["packageName"] = "com.bizcrum.shoekonnect"
        header["sessionToken"] = SessionUtils.getAuthTokens(true)!!
        header["Content-Type"] = "application/json"


        return object : NetworkBoundResourceNoCache<BaseResponse<ArrayList<Product>>>(appExecutors) {

            override fun createCall(): LiveData<ApiResponse<BaseResponse<ArrayList<Product>>>> {
                return webService.loadApprovedProducts(header, JsonObject().apply {
                    addProperty("searchKey", data["searchKey"]!!)
                    addProperty("count", data["count"]!!)
                    addProperty("start", data["start"]!!)
                })
            }
        }.asLiveData()
    }

    fun loadColors() : LiveData<Resource<ColorData>> {
        AppConstants.HOST = AppConstants.HOST_DEV4
        AppConstants.PORT = 80
        val header = HashMap<String, String>()
        header["sessionToken"] = "IlNLMTQ5MDI3MTA2NDE1NjYwNTUwNjUi:1hz0Se:SpzvNCcH2GsDdJSVukbZhZiJb3U"
        header["platform"] = "postman"
        header["packageName"] = "com.bizcrum.shoekonnect"
        header["isAuthRequired"] = "true"
        header["Content-Type"] = "application/json"

        val params = HashMap<String, String>()
        params[""] = ""

        return object : NetworkBoundResourceNoCache<ColorData>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<ColorData>> {
                return webService.loadColors(header, params)
            }

        }.asLiveData()

    }

    fun uploadImageToS3(data: HashMap<String, String>) : LiveData<Resource<DataImageUpload>> {

        AppConstants.HOST = AppConstants.HOST_DEV4
        AppConstants.PORT = 80
        val header = HashMap<String, String>()
        header["sessionToken"] = "0"
        header["platform"] = "android"
        header["packageName"] = "com.bizcrum.shoekonnect"
        header["isAuthRequired"] = "false"
        header["Content-Type"] = "application/x-www-form-urlencoded"
        header["deviceId"] = "ef2e3581-466b-48b4-93cb-6a6f9f2b0848"

        return object : NetworkBoundResourceNoCache<DataImageUpload>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<DataImageUpload>> {
                return webService.uploadImage(header, data)
            }

        }.asLiveData()

    }
}


