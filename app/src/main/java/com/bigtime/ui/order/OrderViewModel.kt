package com.bigtime.ui.order
/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bigtime.common.AbsentLiveData
import com.bigtime.data.api.BaseResponse
import com.bigtime.data.api.Resource
import com.bigtime.data.model.Order
import com.bigtime.data.model.User
import com.bigtime.repo.UMSRepository
import javax.inject.Inject

class OrderViewModel
@Inject constructor( repoRepository: UMSRepository) : ViewModel() {


    private val orderRequestLiveData = MutableLiveData<HashMap<String, String>>()

    fun loadOrderDetails(data: HashMap<String, String>?) {
        orderRequestLiveData.value = data
    }

    val orderResponseLiveData: LiveData<Resource<BaseResponse<ArrayList<Order>>>> =
            Transformations.switchMap(orderRequestLiveData) { data ->
                if (data == null) {
                    AbsentLiveData.create()
                } else {
                    repoRepository.loadOrderDetais(data)
                }
            }



}