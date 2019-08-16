package com.bigtime.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigtime.R
import com.bigtime.data.api.Status
import com.bigtime.data.api.StatusCode
import com.bigtime.databinding.FragmentVerifyPhoneBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.utils.CommonUtils
import com.bigtime.widget.CustomDialog


class VerifyPhoneFragment : BaseFragment<FragmentVerifyPhoneBinding>() {


    companion object {
        const val TAG: String = "RegistrationOtpFragment"
        const val FROM_SIGN_UP: Int = 1
        const val FROM_FORGOT_PASSWORD: Int = 2
    }

    private lateinit var mSignUpViewModel: LoginViewModel
    private var page: Int = 0
    private var phone: String = ""
    private var countryCode: String = ""


    override fun getLayoutId(): Int {
        return R.layout.fragment_verify_phone
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) return

        mSignUpViewModel = getViewModel(LoginViewModel::class.java)
        mBinding.layoutBinder = this

        /**
         * FROM WHICH PAGE
         * 1 -> From Sign Up
         * 2 -> From Forgot Password
         */
        page = VerifyPhoneFragmentArgs.fromBundle(arguments!!).fromWhichPage
        phone = VerifyPhoneFragmentArgs.fromBundle(arguments!!).phone
        countryCode = VerifyPhoneFragmentArgs.fromBundle(arguments!!).countryCode

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mBinding.toolbar.setNavigationOnClickListener { if (!mBinding.isLoading) findNavController().navigateUp() }

        mBinding.tvTitle2.text = getString(R.string.we_sent_to_you, countryCode, phone)

        mBinding.btnResendOtp.setOnClickListener {
            mBinding.isLoading = true
            if (page == FROM_SIGN_UP) {
                val data = HashMap<String, String>()
                data["country_code"] = countryCode
                data["phone"] = phone
                mSignUpViewModel.sentOtp(data)
            } else {
                val data = HashMap<String, String>()
                data["country_code"] = countryCode
                data["phone"] = phone
                mSignUpViewModel.forgotPassword(data)
            }
        }

        mBinding.btnChangeNumber.setOnClickListener { if (!mBinding.isLoading) findNavController().navigateUp() }

        mBinding.btnContinue.setOnClickListener {
            if (mBinding.pinView.text!!.toString().length == 4) {
                mBinding.isLoading = true
                val data = HashMap<String, String>()
                data["otp"] = mBinding.pinView.text!!.toString().trim()
                data["country_code"] = countryCode
                data["phone"] = phone

                when (page) {
                    FROM_SIGN_UP -> mSignUpViewModel.verifyOTP(data)
                    FROM_FORGOT_PASSWORD -> mSignUpViewModel.forgotPasswordValidateOTP(data)
                }

            } else {
                showSnackBar(getString(R.string.enter_valid_otp))
            }
        }

        mSignUpViewModel.sentOtpResponseLiveData.removeObservers(this)
        mSignUpViewModel.sentOtpResponseLiveData.observe(this, Observer { response ->

            if (response == null || response.status == Status.LOADING) {
                return@Observer
            }

            mBinding.isLoading = false

            when {
                response.data == null -> {
                    showSnackBar(response.message!!)
                }

                response.data.status -> {
                    showSnackBar(response.data.message)
                }

                else -> {
                    showSnackBar(response.data.message)
                }
            }

            mSignUpViewModel.sentOtp(null)

        })

        mSignUpViewModel.verifyOTPResponseLiveData.removeObservers(this)
        mSignUpViewModel.verifyOTPResponseLiveData.observe(this, Observer { response ->

            if (response == null || response.status == Status.LOADING) {
                return@Observer
            }

            mBinding.isLoading = false

            when {
                response.data == null -> {
                    showSnackBar(response.message!!)
                }

                response.data.status -> {

                    /*findNavController().navigate(
                            RegistrationOtpFragmentDirections.actionOtpToRegistration(
                                    countryCode,
                                    phone
                            )
                    )*/

                }

                else -> {
                    showSnackBar(response.data.message)
                }
            }

            mSignUpViewModel.verifyOTP(null)

        })

        mSignUpViewModel.forgotPasswordValidateOTPResponseLiveData.removeObservers(this)
        mSignUpViewModel.forgotPasswordValidateOTPResponseLiveData.observe(this, Observer { response ->

            if (response == null || response.status == Status.LOADING) {
                return@Observer
            }

            mBinding.isLoading = false

            when {
                response.data == null -> {
                    showSnackBar(response.message!!)
                }

                response.data.status -> {

                   /* findNavController().navigate(
                            RegistrationOtpFragmentDirections.actionOtpToResetPassword(
                                    countryCode,
                                    phone
                            )
                    )*/

                }

                else -> {
                    showSnackBar(response.data.message)
                }
            }

            mSignUpViewModel.forgotPasswordValidateOTP(null)

        })

        mSignUpViewModel.forgotPasswordResponseLiveData.removeObservers(this)
        mSignUpViewModel.forgotPasswordResponseLiveData.observe(this, Observer { response ->

            if (response == null || response.status == Status.LOADING) {
                return@Observer
            }

            mBinding.isLoading = false

            when {
                response.data == null -> {
                    showSnackBar(response.message!!)
                }

                response.data.status -> {
                    showSnackBar(response.data.message)
                }

                else -> {
                    showSnackBar(response.data.message)
                }
            }

            mSignUpViewModel.forgotPassword(null)

        })

    }


  }