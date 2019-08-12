package com.bigtime.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigtime.R
import com.bigtime.databinding.FragmentLoginBinding
import com.bigtime.ui.BaseFragment
import com.smb.smbapplication.R
import com.smb.smbapplication.data.api.Status
import com.smb.smbapplication.databinding.FragmentLoginBinding
import com.smb.smbapplication.ui.BaseFragment
import com.smb.smbapplication.ui.home.HomeActivity
import com.smb.smbapplication.ui.loginsignup.LoginSignUpViewModel
import com.smb.smbapplication.utils.CommonUtils
import com.smb.smbapplication.utils.SessionUtils
import kotlinx.android.synthetic.main.fragment_choose_product.*
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

        activity!!.window.statusBarColor = ContextCompat.getColor(activity!!, R.color.colorWhite)

        mSignUpViewModel = getViewModel(LoginSignUpViewModel::class.java)
        mBinding.layoutBinder = this

        countryCode = LoginFragmentArgs.fromBundle(arguments!!).countryCode
        phone = LoginFragmentArgs.fromBundle(arguments!!).phone

        getFCMTokens()

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        til_password.typeface = CommonUtils.FONT_METROPOLIS_REGULAR(activity!!)

        tv_forgot_password.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginToForgotPassword(countryCode, phone))
        }

        btn_login.setOnClickListener {

            tiet_password.addTextChangedListener(generalTextWatcher)

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

                response.data.status -> {

                    mBinding.btnLogin.loadingSuccessful()

                    val session = response.data.data

                    SessionUtils.setIsLoggedIn(true)
                    SessionUtils.saveUserPassword(tiet_password.text.toString().trim())
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

        if (tiet_password.text.toString().trim().isEmpty()) {
            til_password.error = getString(R.string.password_required)
            tiet_password.requestFocus()
        } else {
            dismissKeyboard(btn_login.windowToken)
            mBinding.isLoading = true
            mBinding.btnLogin.startLoading()
            val data = HashMap<String, String>()
            data["country_code"] = countryCode
            data["phone"] = phone
            data["password"] = tiet_password.text.toString().trim()

            mSignUpViewModel.login(data)
        }

    }

    private val generalTextWatcher = object : TextWatcher {

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            til_password.error = null
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable) {}

    }

}