package com.bigtime.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigtime.R
import com.bigtime.databinding.FragmentVerifyPhoneBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.utils.CommonUtils
import com.smb.smbapplication.R
import com.smb.smbapplication.data.api.Status
import com.smb.smbapplication.data.api.StatusCode
import com.smb.smbapplication.databinding.FragmentVerifyPhoneBinding
import com.smb.smbapplication.ui.BaseFragment
import com.smb.smbapplication.ui.loginsignup.LoginSignUpViewModel
import com.smb.smbapplication.utils.CommonUtils
import com.smb.smbapplication.widget.CustomDialog
import kotlinx.android.synthetic.main.fragment_verify_phone.*

/**
 * Created by Ȿ₳Ɲ @ NEWAGESMB on Tuesday, May 21, 2019
 */

class VerifyPhoneFragment : BaseFragment<FragmentVerifyPhoneBinding>() {

    companion object {
        const val TAG: String = "VerifyPhoneFragment"
    }

    lateinit var mSignUpViewModel: LoginViewModel


    override fun getLayoutId(): Int {
        return R.layout.fragment_verify_phone
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) return

        activity!!.window.statusBarColor = ContextCompat.getColor(activity!!, R.color.colorWhite)

        mSignUpViewModel = getViewModel(LoginSignUpViewModel::class.java)
        mBinding.layoutBinder = this

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        ccp_phone.setTypeFace(CommonUtils.FONT_METROPOLIS_REGULAR(activity!!))
        ccp_phone.registerCarrierNumberEditText(tiet_phone)

        btn_continue.setOnClickListener {

            tiet_phone.addTextChangedListener(GenericTextWatcher(tiet_phone))

            if (tiet_phone.text.toString().trim().isEmpty()) {
                setErrorOnPhone(getString(R.string.phone_required))
                tiet_phone.requestFocus()
            } else if (!ccp_phone.isValidFullNumber) {
                setErrorOnPhone(getString(R.string.phone_invalid))
                tiet_phone.requestFocus()
            } else {

                dismissKeyboard(btn_continue.windowToken)

                mBinding.isLoading = true
                btn_continue.startLoading()
                val data = HashMap<String, String>()
                data["country_code"] = ccp_phone.selectedCountryCodeWithPlus
                data["phone"] = CommonUtils.getUnformattedPhoneNumber(tiet_phone.text.toString().trim())

                mSignUpViewModel.verifyPhone(data)
            }

        }

        mSignUpViewModel.verifyPhoneResponseLiveData.removeObservers(this)
        mSignUpViewModel.verifyPhoneResponseLiveData.observe(this, Observer { response ->

            if (response == null || response.status == Status.LOADING) {
                return@Observer
            }

            mBinding.isLoading = false

            when {
                response.data == null -> {
                    btn_continue.loadingFailed()
                    showSnackBar(response.message!!)
                }

                response.data.status -> {
                    btn_continue.loadingSuccessful()
                    findNavController().navigate(
                        VerifyPhoneFragmentDirections.actionVerifyPhoneToLogin(
                            ccp_phone.selectedCountryCodeWithPlus,
                            CommonUtils.getUnformattedPhoneNumber(tiet_phone.text.toString().trim())
                        )
                    )
                }

                else -> {
                    btn_continue.loadingFailed()

                    if(!isApiCommonError(response.data.statusCode, response.data.message)){

                        when (response.data.statusCode) {
                            StatusCode.ACCOUNT_NOT_FOUND -> {
                                CustomDialog.with(
                                    activity!!,
                                    R.drawable.ic_close,
                                    getString(R.string.hello),
                                    getString(R.string.alert_to_sign_up),
                                    true,
                                    getString(R.string.sign_up), object : CustomDialog.ICustomDialogListener {
                                        override fun onActionClicked() {
                                            findNavController().navigate(
                                                VerifyPhoneFragmentDirections.actionVerifyPhoneToSignUp(
                                                    ccp_phone.selectedCountryCodeWithPlus,
                                                    CommonUtils.getUnformattedPhoneNumber(tiet_phone.text.toString().trim())
                                                )
                                            )
                                        }

                                    }
                                )
                            }

                            else -> showSnackBar(response.data.message)
                        }
                    }

                }
            }

            mSignUpViewModel.verifyPhone(null)
        })

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

    private inner class GenericTextWatcher internal constructor(private val view: View) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            when (view.id) {
                R.id.tiet_phone -> setErrorOnPhone(invalidate = true)
            }
        }

        override fun afterTextChanged(editable: Editable) {}
    }


}