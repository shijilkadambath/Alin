package com.bigtime.repo

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.bigtime.AppExecutors
import com.bigtime.data.api.*


/**
 * Created by Smirk_1120 on 01,November,2018
 * tonyaugustine47@gmail.com
 */
abstract class NetworkBoundResourceNoCache<RequestType> @MainThread constructor(private val appExecutors: AppExecutors){

    private val result = MediatorLiveData<Resource<RequestType>>()

    init {
        result.value = Resource.loading(null)
        fetchFromNetwork()
    }

    @MainThread
    private fun setValue(newValue: Resource<RequestType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.mainThread().execute {
                        // we specially request a new live data,
                        // otherwise we will get immediately last cached value,
                        // which may not be updated with latest results received from network.
                        setValue(Resource.success(processResponse(response)))
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        setValue(Resource.success(null))
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    setValue(Resource.error(response.errorMessage, null))
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<RequestType>>

    @WorkerThread
    protected open fun processResponse(response : ApiSuccessResponse<RequestType>) = response.body

    @MainThread
    protected abstract fun createCall() : LiveData<ApiResponse<RequestType>>
}