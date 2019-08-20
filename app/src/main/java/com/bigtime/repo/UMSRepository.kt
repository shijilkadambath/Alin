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
import com.bigtime.data.model.Brands
import com.bigtime.data.model.User
import com.bigtime.utils.SessionUtils
import com.google.gson.JsonObject
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
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

                        result.setValue(BaseResponse("",1,"",StatusCode.OK,list))
                })

                return  result
            }

            override fun createCall() = webService.loadUsers()

        }.asLiveData()
    }



    fun resetPassword(data: HashMap<String, String>): LiveData<Resource<BaseResponse<Any>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun forgotPasswordValidateOTP(data: HashMap<String, String>): LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun forgotPassword(data: HashMap<String, String>): LiveData<Resource<BaseResponse<Any>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun login(data: HashMap<String, String>): LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun verifyPhone(data: HashMap<String, String>): LiveData<Resource<BaseResponse<Any>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun registration(data: HashMap<String, String>): LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun verifyOTP(data: HashMap<String, String>): LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun sentOtp(data: HashMap<String, String>): LiveData<Resource<BaseResponse<Any>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getProfile(): LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun loadBrands(data: HashMap<String, String>): LiveData<Resource<BaseResponse<Brands>>> {
        return object : NetworkBoundResourceNoCache<BaseResponse<Brands>>(appExecutors) {
            override fun createCall(): LiveData<ApiResponse<BaseResponse<Brands>>> {
                return webService.loadBrands(data, JsonObject().apply {
                    addProperty("a", "b")
                })
            }
        }.asLiveData()
    }
}
