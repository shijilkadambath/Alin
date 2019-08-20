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
import com.bigtime.databinding.FragmentLoginBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.ui.home.HomeActivity
import com.bigtime.utils.CommonUtils
import com.bigtime.utils.SessionUtils
import org.jetbrains.anko.support.v4.intentFor

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 *
 * Updated by Ȿ₳Ɲ @ NEWAGESMB on Thursday, April 11, 2019
 */

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    companion object {
        const val TAG: String = "LoginFragment"
    }

    lateinit var mSignUpViewModel: LoginViewModel


    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    private var countryCode = ""
    private var phone = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) return

        activity!!.window.statusBarColor = ContextCompat.getColor(activity!!, R.color.white)

        mSignUpViewModel = getViewModel(LoginViewModel::class.java)
        mBinding.layoutBinder = this

        //countryCode = LoginFragmentArgs.fromBundle(arguments!!).countryCode
        //phone = LoginFragmentArgs.fromBundle(arguments!!).phone

        getFCMTokens()

        // mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        //mBinding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        mBinding.ccpPhone.setTypeFace(CommonUtils.FONT_METROPOLIS_REGULAR(activity!!))
        mBinding.ccpPhone.registerCarrierNumberEditText(mBinding.tietPhone)

        if (countryCode.isNotEmpty()) mBinding.ccpPhone.setCountryForPhoneCode(countryCode.replace("+","").toInt())
        if (phone.isNotEmpty()) mBinding.tietPhone.setText(phone)

        mBinding.tilPassword.typeface = CommonUtils.FONT_METROPOLIS_REGULAR(activity!!)

        mBinding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginToForgotPassword(countryCode, phone))
        }

        mBinding.btnLogin.setOnClickListener {

            mBinding.tietPassword.addTextChangedListener(generalTextWatcher)

            callLogin()

        }

        mSignUpViewModel.loginResponseLiveData.removeObservers(this)
        mSignUpViewModel.loginResponseLiveData.observe(this, Observer { response ->

            if (response == null || response.status == Status.LOADING) {
                return@Observer
            }

            mBinding.isLoading = false

            when {
                response.data == null -> {
                    mBinding.btnLogin.loadingFailed()
                    showSnackBar(response.message!!)
                }

                response.data.status == 1 -> {

                    mBinding.btnLogin.loadingSuccessful()

                    val session = response.data.data

                    SessionUtils.setIsLoggedIn(true)
                    SessionUtils.saveUserPassword(mBinding.tietPassword.text.toString().trim())
                    SessionUtils.saveSession(session)

                    startActivity(intentFor<HomeActivity>())
                    activity!!.finishAffinity()

                }

                else -> {
                    mBinding.btnLogin.loadingFailed()
                    showSnackBar(response.data.message)
                }
            }

            mSignUpViewModel.login(null)
        })

    }

    private fun callLogin() {

        mBinding.tietPhone.addTextChangedListener(GenericTextWatcher(mBinding.tietPhone))

        if (mBinding.tietPhone.text.toString().trim().isEmpty()) {
            setErrorOnPhone(getString(R.string.phone_required))
        } else if (!mBinding.ccpPhone.isValidFullNumber) {
            setErrorOnPhone(getString(R.string.phone_invalid))
        }else if (mBinding.tietPassword.text.toString().trim().isEmpty()) {
            mBinding.tilPassword.error = getString(R.string.password_required)
            mBinding.tietPassword.requestFocus()
        } else {
            dismissKeyboard(mBinding.btnLogin.windowToken)
            mBinding.isLoading = true
            mBinding.btnLogin.startLoading()

            countryCode = mBinding.ccpPhone.selectedCountryCodeWithPlus
            phone = CommonUtils.getUnformattedPhoneNumber(mBinding.tietPhone.text.toString().trim())
            val data = HashMap<String, String>()
            //data["username"] = countryCode+phone
            data["username"] = phone
            data["devMode"] = "1"
            data["pword"] = mBinding.tietPassword.text.toString().trim()

            mSignUpViewModel.login(data)
        }
    }

    private val generalTextWatcher = object : TextWatcher {

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            mBinding.tilPassword.error = null
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable) {}

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