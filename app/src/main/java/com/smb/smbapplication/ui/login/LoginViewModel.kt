package com.smb.smbapplication.ui.login
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smb.smbapplication.common.AbsentLiveData
import com.smb.smbapplication.data.api.BaseResponse
import com.smb.smbapplication.data.api.Resource
import com.smb.smbapplication.data.model.User
import com.smb.smbapplication.repo.UMSRepository
import javax.inject.Inject

class LoginViewModel
@Inject constructor( repoRepository: UMSRepository) : ViewModel() {

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