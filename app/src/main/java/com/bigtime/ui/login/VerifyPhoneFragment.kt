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
        const val TAG: String = "VerifyPhoneFragment"
    }

    lateinit var mSignUpViewModel: LoginViewModel


    override fun getLayoutId(): Int {
        return R.layout.fragment_verify_phone
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) return

        activity!!.window.statusBarColor = ContextCompat.getColor(activity!!, R.color.white)

        mSignUpViewModel = getViewModel(LoginViewModel::class.java)
        mBinding.layoutBinder = this

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mBinding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        mBinding.ccpPhone.setTypeFace(CommonUtils.FONT_METROPOLIS_REGULAR(activity!!))
        mBinding.ccpPhone.registerCarrierNumberEditText(mBinding.tietPhone)

        mBinding.btnContinue.setOnClickListener {

            mBinding.tietPhone.addTextChangedListener(GenericTextWatcher(mBinding.tietPhone))

            if (mBinding.tietPhone.text.toString().trim().isEmpty()) {
                setErrorOnPhone(getString(R.string.phone_required))
                mBinding.tietPhone.requestFocus()
            } else if (!mBinding.ccpPhone.isValidFullNumber) {
                setErrorOnPhone(getString(R.string.phone_invalid))
                mBinding.tietPhone.requestFocus()
            } else {

                dismissKeyboard(mBinding.btnContinue.windowToken)

                mBinding.isLoading = true
                mBinding.btnContinue.startLoading()
                val data = HashMap<String, String>()
                data["country_code"] = mBinding.ccpPhone.selectedCountryCodeWithPlus
                data["phone"] = CommonUtils.getUnformattedPhoneNumber(mBinding.tietPhone.text.toString().trim())

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
                    mBinding.btnContinue.loadingFailed()
                    showSnackBar(response.message!!)
                }

                response.data.status -> {
                    mBinding.btnContinue.loadingSuccessful()
                    /*findNavController().navigate(
                        VerifyPhoneFragmentDirections.actionVerifyPhoneToLogin(
                                mBinding.ccpPhone.selectedCountryCodeWithPlus,
                            CommonUtils.getUnformattedPhoneNumber(mBinding.tietPhone.text.toString().trim())
                        )
                    )*/
                }

                else -> {
                    mBinding.btnContinue.loadingFailed()

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
                                            /*findNavController().navigate(
                                                VerifyPhoneFragmentDirections.actionVerifyPhoneToSignUp(
                                                        mBinding.ccpPhone.selectedCountryCodeWithPlus,
                                                    CommonUtils.getUnformattedPhoneNumber(mBinding.tiet_phone.text.toString().trim())
                                                )
                                            )*/
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
            mBinding.tvPhoneHint.setTextColor(ContextCompat.getColor(activity!!, R.color.primaryText))
            mBinding.tvPhoneError.visibility = View.INVISIBLE
        } else {
            mBinding.tvPhoneHint.setTextColor(ContextCompat.getColor(activity!!, android.R.color.holo_red_light))
            mBinding.tvPhoneError.visibility = View.VISIBLE
            mBinding.tvPhoneError.text = message
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