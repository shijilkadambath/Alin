package com.bigtime.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bigtime.common.AbsentLiveData
import com.bigtime.data.api.BaseResponse
import com.bigtime.data.api.Resource
import com.bigtime.data.model.User
import com.bigtime.repo.UMSRepository
import com.bigtime.utils.SessionUtils
import javax.inject.Inject

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 *
 * Updated by Ȿ₳Ɲ @ NEWAGESMB on Tuesday, May 07, 2019
 */

class HomeViewModel
@Inject constructor(repoRepository: UMSRepository) : ViewModel() {


    private val _login = MutableLiveData<String>()

    val login: LiveData<String>
        get() = _login

    val repositories: LiveData<Resource<BaseResponse<List<User>>>> = Transformations
            .switchMap(_login) { login ->
                if (login == null) {
                    AbsentLiveData.create()
                } else {
                    repoRepository.loadUsers()
                }
            }


    fun retry() {
        _login.value?.let {
            _login.value = it
        }
    }

    fun loadData() {
        //if (_login.value != login) {
        _login.value = "test"
        //}
    }

}