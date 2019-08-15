package com.bigtime.ui.pending_product
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bigtime.common.AbsentLiveData
import com.bigtime.data.api.BaseResponse
import com.bigtime.data.api.Resource
import com.bigtime.data.model.User
import com.bigtime.repo.UMSRepository
import javax.inject.Inject

class PendingProductViewModel
@Inject constructor( repoRepository: UMSRepository) : ViewModel() {

}