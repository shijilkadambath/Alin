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
import com.bigtime.databinding.FragmentForgotPasswordBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.utils.CommonUtils

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 *
 */

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    companion object {
        const val TAG: String = "ForgotPasswordFragment"
    }

    lateinit var mSignUpViewModel: LoginViewModel
    private var password: String = ""
    private var phone: String = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_forgot_password
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) return

        mSignUpViewModel = getViewModel(LoginViewModel::class.java)
        mBinding.layoutBinder = this

        //countryCode = ForgotPasswordFragmentArgs.fromBundle(arguments!!).countryCode
        phone = ForgotPasswordFragmentArgs.fromBundle(arguments!!).phone

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mBinding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        mBinding.ccpPhone.setTypeFace(CommonUtils.FONT_METROPOLIS_REGULAR(activity!!))
        mBinding.ccpPhone.registerCarrierNumberEditText(mBinding.tietPhone)

        //if (countryCode.isNotEmpty()) mBinding.ccpPhone.setCountryForPhoneCode(countryCode.replace("+","").toInt())
        if (phone.isNotEmpty()) mBinding.tietPhone.setText(phone)

        mBinding.btnContinue.setOnClickListener {

            mBinding.tilPassword.isErrorEnabled=false
            mBinding.tietPhone.addTextChangedListener(GenericTextWatcher(mBinding.tietPhone))

            password=mBinding.tietPassword.text.toString().trim()

            if (mBinding.tietPhone.text.toString().trim().isEmpty()) {
                setErrorOnPhone(getString(R.string.phone_required))
            } else if (!mBinding.ccpPhone.isValidFullNumber) {
                setErrorOnPhone(getString(R.string.phone_invalid))
            }else if (password.isEmpty()) {
                mBinding.tilPassword.isErrorEnabled=true
                mBinding.tilPassword.error = getString(R.string.password_required)
                mBinding.tietPassword.requestFocus()
            } else {
                dismissKeyboard(mBinding.btnContinue.windowToken)
                mBinding.isLoading = true
                //countryCode = mBinding.ccpPhone.selectedCountryCodeWithPlus

                phone = CommonUtils.getUnformattedPhoneNumber(mBinding.tietPhone.text.toString().trim())
                val data = HashMap<String, String>()
                //data["country_code"] = countryCode
                data["phoneNumber"] = phone
                mSignUpViewModel.forgotPassword(data)
            }

        }

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

                response.data!!.isSuccess() -> {
                    /*findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordToOtp(
                            fromWhichPage = RegistrationOtpFragment.FROM_FORGOT_PASSWORD,
                            phone = phone, countryCode = countryCode
                        )
                    )*/

                    findNavController().navigate(ForgotPasswordFragmentDirections.actionBackToVerifyPhone(phone,
                            password,response.data.data!!,response.data.token))
                }

                else -> {
                    showSnackBar(response.data.message)
                }
            }

            mSignUpViewModel.forgotPassword(null)

        })
    }

    private inner class GenericTextWatcher internal constructor(private val view: View) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            when (view.id) {
                R.id.tiet_phone -> setErrorOnPhone(invalidate = true)
            }
        }

        override fun afterTextChanged(editable: Editable) {}
    }


    private fun setErrorOnPhone(message: String = "", invalidate: Boolean = false) {
        if (invalidate) {
            mBinding.tvPhoneHint.setTextColor(ContextCompat.getColor(activity!!, R.color.primaryText))
            mBinding.tvPhoneError.visibility = View.INVISIBLE
        } else {
            mBinding.tvPhoneHint.setTextColor(ContextCompat.getColor(activity!!, android.R.color.holo_red_light))
            mBinding.tvPhoneError.visibility = View.VISIBLE
            mBinding.tvPhoneError.text = message
        }
    }
}
