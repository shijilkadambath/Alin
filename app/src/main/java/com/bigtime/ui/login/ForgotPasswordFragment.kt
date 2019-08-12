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
 *
 * Updated by Ȿ₳Ɲ @ NEWAGESMB on Thursday, April 11, 2019
 */

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    companion object {
        const val TAG: String = "ForgotPasswordFragment"
    }

    lateinit var mSignUpViewModel: LoginViewModel
    private var countryCode: String = ""
    private var phone: String = ""

    override fun getLayoutId(): Int {
        return R.layout.fragment_forgot_password
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) return

        mSignUpViewModel = getViewModel(LoginViewModel::class.java)
        mBinding.layoutBinder = this

        countryCode = ForgotPasswordFragmentArgs.fromBundle(arguments!!).countryCode
        phone = ForgotPasswordFragmentArgs.fromBundle(arguments!!).phone

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        ccp_phone.setTypeFace(CommonUtils.FONT_METROPOLIS_REGULAR(activity!!))
        ccp_phone.registerCarrierNumberEditText(tiet_phone)

        if (countryCode.isNotEmpty()) ccp_phone.setCountryForPhoneCode(countryCode.replace("+","").toInt())
        if (phone.isNotEmpty()) tiet_phone.setText(phone)

        btn_continue.setOnClickListener {

            tiet_phone.addTextChangedListener(GenericTextWatcher(tiet_phone))

            if (tiet_phone.text.toString().trim().isEmpty()) {
                setErrorOnPhone(getString(R.string.phone_required))
            } else if (!ccp_phone.isValidFullNumber) {
                setErrorOnPhone(getString(R.string.phone_invalid))
            } else {
                mBinding.isLoading = true
                countryCode = ccp_phone.selectedCountryCodeWithPlus
                phone = CommonUtils.getUnformattedPhoneNumber(tiet_phone.text.toString().trim())
                val data = HashMap<String, String>()
                data["country_code"] = countryCode
                data["phone"] = phone
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

                response.data.status -> {
                    findNavController().navigate(
                        ForgotPasswordFragmentDirections.actionForgotPasswordToOtp(
                            fromWhichPage = RegistrationOtpFragment.FROM_FORGOT_PASSWORD,
                            phone = phone, countryCode = countryCode
                        )
                    )
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
            tv_phone_hint.setTextColor(ContextCompat.getColor(activity!!, R.color.colorAlphaAppTextBlue))
            tv_phone_error.visibility = View.INVISIBLE
        } else {
            tv_phone_hint.setTextColor(ContextCompat.getColor(activity!!, android.R.color.holo_red_light))
            tv_phone_error.visibility = View.VISIBLE
            tv_phone_error.text = message
        }
    }
}
