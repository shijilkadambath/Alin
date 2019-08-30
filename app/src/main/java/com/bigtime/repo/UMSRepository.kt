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
import com.bigtime.data.model.Brand
import com.bigtime.data.model.MainCategory
import com.bigtime.data.model.Order
import com.bigtime.data.model.User
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

    fun loadUsers(): LiveData<Resource<BaseResponse<List<User>>>> {


        return object : NetworkBoundResource<BaseResponse<List<User>>, BaseResponse<List<User>>>(appExecutors) {
            override fun saveCallResult(item: BaseResponse<List<User>>) {

                if (item.isSuccess() && item.data!=null) {
                    umsoDao.insertUsers(item.data)
                }
            }

            override fun shouldFetch(data: BaseResponse<List<User>> ?): Boolean {
               // return data == null || !data.isSuccess() ||data.data ==null|| data.data.isEmpty()
                return true
            }

            override fun loadFromDb(): LiveData<BaseResponse<List<User>>> {

                val result = MediatorLiveData<BaseResponse<List<User>>>()


               result.addSource(umsoDao.loadUsers(), Observer {
                        list->

                        result.setValue(BaseResponse("",1,"",StatusCode.OK,"",list))
                })

                return  result
            }

            override fun createCall() = webService.loadUsers()

        }.asLiveData()
    }


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

    fun getProfile(): LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun loadBrands(data: HashMap<String, String>): LiveData<Resource<BaseResponseTwo<ArrayList<Brand>,ArrayList<MainCategory>>>> {
        AppConstants.HOST = AppConstants.HOST_LOGIN
        AppConstants.PORT = 4000

        return object : NetworkBoundResourceNoCache<BaseResponseTwo<ArrayList<Brand>, ArrayList<MainCategory>>>(appExecutors) {

            override fun createCall(): LiveData<ApiResponse<BaseResponseTwo<ArrayList<Brand>,ArrayList<MainCategory>>>> {
                return webService.loadBrands(data, JsonObject().apply {
                    addProperty("a", "b")
                })
            }
        }.asLiveData()
    }


        fun loadOrderDetais(data: HashMap<String, String>): LiveData<Resource<BaseResponse<Order>>> {

            val header = HashMap<String, String>()
            header["SOURCE"] = "cCX1G9EVpL"
            header["PLATFORM"] = "Android"
            header["PACKAGE-NAME"] = "com.bizcrum.shoekonnect"
            header["SKAUTH-TOKEN"] = data["token"]!!
            header["Content-Type"] = "application/json"

            AppConstants.HOST = AppConstants.HOST_LOGIN
            AppConstants.PORT = 4000
            return object : NetworkBoundResourceNoCache<BaseResponse<Order>>(appExecutors) {

                override fun createCall(): LiveData<ApiResponse<BaseResponse<Order>>> {
                    return webService.loadOrderDetails(header, JsonObject().apply {
                        addProperty("userID", data["userID"]!!)
                    })
                }
            }.asLiveData()
        }
}
