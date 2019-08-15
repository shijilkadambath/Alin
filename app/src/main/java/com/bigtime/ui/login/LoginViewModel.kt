package com.bigtime.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bigtime.common.AbsentLiveData
import com.bigtime.data.api.BaseResponse
import com.bigtime.data.api.Resource
import com.bigtime.repo.UMSRepository
import com.bigtime.utils.SessionUtils
import javax.inject.Inject

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 *
 */

class LoginViewModel
@Inject constructor(repoRepository: UMSRepository) : ViewModel() {

    /*** For send OTP ***/
    private val sentOtpLiveData = MutableLiveData<HashMap<String, String>>()

    fun sentOtp(data: HashMap<String, String>?) {
        sentOtpLiveData.value = data
    }

    val sentOtpResponseLiveData: LiveData<Resource<BaseResponse<Any>>> =
        Transformations.switchMap(sentOtpLiveData) { data ->
            if (data == null) {
                AbsentLiveData.create()
            } else {
                repoRepository.sentOtp(data)
            }
        }
    /**********/

    /*** For Verify OTP ***/
    private val verifyOTPRequestLiveData = MutableLiveData<HashMap<String, String>>()

    fun verifyOTP(data: HashMap<String, String>?) {
        verifyOTPRequestLiveData.value = data
    }

    val verifyOTPResponseLiveData: LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> =
        Transformations.switchMap(verifyOTPRequestLiveData) { data ->
            if (data == null) {
                AbsentLiveData.create()
            } else {
                repoRepository.verifyOTP(data)
            }
        }
    /**********/

    /*** For Registration ***/
    private val registrationRequestLiveData = MutableLiveData<HashMap<String, String>>()

    fun registration(data: HashMap<String, String>?) {
        registrationRequestLiveData.value = data
    }

    val registrationResponseLiveData: LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> =
        Transformations.switchMap(registrationRequestLiveData) { data ->
           if (data == null) {
                AbsentLiveData.create()
            } else {
                repoRepository.registration(data)
            }
        }
    /**********/

    /*** For Verify Phone ***/
    private val verifyPhoneLiveData = MutableLiveData<HashMap<String, String>>()

    fun verifyPhone(data: HashMap<String, String>?) {
        verifyPhoneLiveData.value = data
    }

    val verifyPhoneResponseLiveData: LiveData<Resource<BaseResponse<Any>>> =
        Transformations.switchMap(verifyPhoneLiveData) { data ->
            if (data == null) {
                AbsentLiveData.create()
            } else {
                repoRepository.verifyPhone(data)
            }
        }
    /**********/

    /*** For Login ***/
    private val loginRequestLiveData = MutableLiveData<HashMap<String, String>>()

    fun login(data: HashMap<String, String>?) {
        loginRequestLiveData.value = data
    }

    val loginResponseLiveData: LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> =
        Transformations.switchMap(loginRequestLiveData) { data ->
            if (data == null) {
                AbsentLiveData.create()
            } else {
                repoRepository.login(data)
            }
        }
    /**********/

    /*** For Forgot Password ***/
    private val forgotPasswordRequestLiveData = MutableLiveData<HashMap<String, String>>()

    fun forgotPassword(data: HashMap<String, String>?) {
        forgotPasswordRequestLiveData.value = data
    }

    val forgotPasswordResponseLiveData: LiveData<Resource<BaseResponse<Any>>> =
        Transformations.switchMap(forgotPasswordRequestLiveData) { data ->
            if (data == null) {
                AbsentLiveData.create()
            } else {
                repoRepository.forgotPassword(data)
            }
        }
    /**********/

    /*** For Verify OTP for Forgot Password***/
    private val forgotPasswordValidateOTPRequestLiveData = MutableLiveData<HashMap<String, String>>()

    fun forgotPasswordValidateOTP(data: HashMap<String, String>?) {
        forgotPasswordValidateOTPRequestLiveData.value = data
    }

    val forgotPasswordValidateOTPResponseLiveData: LiveData<Resource<BaseResponse<SessionUtils.LoginSession>>> =
        Transformations.switchMap(forgotPasswordValidateOTPRequestLiveData) { data ->
            if (data == null) {
                AbsentLiveData.create()
            } else {
                repoRepository.forgotPasswordValidateOTP(data)
            }
        }
    /**********/

    /*** For Reset Password ***/
    private val resetPasswordRequestLiveData = MutableLiveData<HashMap<String, String>>()

    fun resetPassword(data: HashMap<String, String>?) {
        resetPasswordRequestLiveData.value = data
    }

    val resetPasswordResponseLiveData: LiveData<Resource<BaseResponse<Any>>> =
        Transformations.switchMap(resetPasswordRequestLiveData) { data ->
            if (data == null) {
                AbsentLiveData.create()
            } else {
                repoRepository.resetPassword(data)
            }
        }
    /**********/

}