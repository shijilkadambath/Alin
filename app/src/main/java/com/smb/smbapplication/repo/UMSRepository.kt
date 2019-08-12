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

package com.smb.smbapplication.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.smb.smbapplication.AppExecutors
import com.smb.smbapplication.data.api.BaseResponse
import com.smb.smbapplication.data.api.WebService
import com.smb.smbapplication.data.api.Resource
import com.smb.smbapplication.data.db.AppDb
import com.smb.smbapplication.data.db.UMSDao
import com.smb.smbapplication.data.model.User
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

                        result.setValue(BaseResponse("",true,list))
                })

                return  result
            }

            override fun createCall() = webService.loadUsers()



        }.asLiveData()
    }




}
