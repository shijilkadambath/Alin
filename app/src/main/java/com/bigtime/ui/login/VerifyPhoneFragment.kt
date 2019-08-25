package com.bigtime.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
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


class VerifyPhoneFragment : BaseFragment<FragmentVerifyPhoneBinding>(), OnBackPressedCallback {


    companion object {
        const val TAG: String = "RegistrationOtpFragment"
        const val FROM_SIGN_UP: Int = 1
        const val FROM_FORGOT_PASSWORD: Int = 2
    }

    private lateinit var mSignUpViewModel: LoginViewModel
    private var page: Int = 0


    private var phone: String = ""
    private var password: String = ""
    private var otp: String = ""
    private var token: String = ""


    override fun getLayoutId(): Int {
        return R.layout.fragment_verify_phone
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) return

        mSignUpViewModel = getViewModel(LoginViewModel::class.java)
        mBinding.layoutBinder = this
        requireActivity().onBackPressedDispatcher.addCallback(this, this)
        /**
         * FROM WHICH PAGE
         * 1 -> From Sign Up
         * 2 -> From Forgot Password
         */
        page = 1;//VerifyPhoneFragmentArgs.fromBundle(arguments!!).password
        phone = VerifyPhoneFragmentArgs.fromBundle(arguments!!).phone
        password = VerifyPhoneFragmentArgs.fromBundle(arguments!!).password
        otp = VerifyPhoneFragmentArgs.fromBundle(arguments!!).otp
        token = VerifyPhoneFragmentArgs.fromBundle(arguments!!).auth

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mBinding.toolbar.setNavigationOnClickListener { if (!mBinding.isLoading) findNavController().navigateUp() }

        //mBinding.tvTitle2.text = getString(R.string.we_sent_to_you, "", phone)
        mBinding.tvPhone.text = phone

        mBinding.btnResendOtp.setOnClickListener {
            mBinding.isLoading = true

            if (page == FROM_SIGN_UP) {
                val data = HashMap<String, String>()
                //data["country_code"] = countryCode
                data["phone"] = phone
               // mSignUpViewModel.sentOtp(data)
            } else {
                val data = HashMap<String, String>()
                //data["country_code"] = countryCode
                data["phone"] = phone
               // mSignUpViewModel.forgotPassword(data)
            }
        }

        mBinding.btnChangeNumber.setOnClickListener { if (!mBinding.isLoading) findNavController().navigateUp() }

        mBinding.btnContinue.setOnClickListener {
            if (mBinding.pinView.text!!.toString().length == 4) {
                mBinding.isLoading = true
                val data = HashMap<String, String>()
                data["otp"] = mBinding.pinView.text!!.toString().trim()
                data["phoneNumber"] = phone
                data["pword"] = password
                data["token"] = token

                mSignUpViewModel.verifyOTP(data)

                /*when (page) {
                    FROM_SIGN_UP -> mSignUpViewModel.verifyOTP(data)
                    FROM_FORGOT_PASSWORD -> mSignUpViewModel.forgotPasswordValidateOTP(data)
                }*/

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

                response.data.status == 1 -> {
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

                response.data.isSuccess() -> {

                    /*findNavController().navigate(
                            RegistrationOtpFragmentDirections.actionOtpToRegistration(
                                    countryCode,
                                    phone
                            )
                    )*/
                    CustomDialog.with(
                            activity!!,
                            R.drawable.ic_tick,
                            getString(R.string.hello),
                            getString(R.string.alert_password_saved),
                            true,
                            getString(R.string.continue_to_login), object : CustomDialog.ICustomDialogListener {
                        override fun onActionClicked() {
                            handleOnBackPressed()
                        }

                    })

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

                response.data.isSuccess() -> {

                   /* findNavController().navigate(
                            RegistrationOtpFragmentDirections.actionOtpToResetPassword(
                                    countryCode,
                                    phone
                            )
                    )*/
                    CustomDialog.with(
                            activity!!,
                            R.drawable.ic_tick,
                            getString(R.string.hello),
                            getString(R.string.alert_password_saved),
                            true,
                            getString(R.string.continue_to_login), object : CustomDialog.ICustomDialogListener {
                        override fun onActionClicked() {
                            handleOnBackPressed()
                        }

                    })

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

                response.data.isSuccess() -> {
                    showSnackBar(response.data.message)
                }

                else -> {
                    showSnackBar(response.data.message)
                }
            }

            mSignUpViewModel.forgotPassword(null)

        })

    }
    override fun handleOnBackPressed(): Boolean {
        findNavController().navigate(R.id.action_back_to_verifyPhone)
        //findNavController().navigate(VerifyPhoneFragmentDirections.actionBackToVerifyPhone())
        return true
    }

  }