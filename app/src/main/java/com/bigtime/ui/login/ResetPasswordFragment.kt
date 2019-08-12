package com.bigtime.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigtime.databinding.FragmentResetPasswordBinding
import com.bigtime.ui.BaseFragment
import com.smb.smbapplication.R
import com.smb.smbapplication.data.api.Status
import com.smb.smbapplication.databinding.FragmentResetPasswordBinding
import com.smb.smbapplication.ui.BaseFragment
import com.smb.smbapplication.ui.loginsignup.LoginSignUpViewModel
import com.smb.smbapplication.utils.CommonUtils
import com.smb.smbapplication.widget.CustomDialog
import kotlinx.android.synthetic.main.fragment_reset_password.*

/**
 * Created by Ȿ₳Ɲ @ NEWAGESMB on Tuesday, April 30, 2019
 */

class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>(), OnBackPressedCallback {

    companion object {
        const val TAG: String = "ResetPasswordFragment"
    }

    private lateinit var mSignUpViewModel: LoginSignUpViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_reset_password
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) return

        mSignUpViewModel = getViewModel(LoginSignUpViewModel::class.java)
        mBinding.layoutBinder = this

        requireActivity().onBackPressedDispatcher.addCallback(this, this)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener { handleOnBackPressed() }

        val countryCode = ResetPasswordFragmentArgs.fromBundle(arguments!!).countryCode
        val phone = ResetPasswordFragmentArgs.fromBundle(arguments!!).phone

        til_password.typeface = CommonUtils.FONT_METROPOLIS_REGULAR(activity!!)
        til_confirm_password.typeface = CommonUtils.FONT_METROPOLIS_REGULAR(activity!!)

        btn_submit.setOnClickListener {

            tiet_password.addTextChangedListener(GenericTextWatcher(tiet_password))
            tiet_confirm_password.addTextChangedListener(GenericTextWatcher(tiet_confirm_password))

            if (tiet_password.text.toString().trim().isEmpty()) {
                til_password.error = getString(R.string.password_required)
            } else if (!CommonUtils.PASSWORD.matcher(tiet_password.text.toString().trim()).matches()) {
                til_password.isPasswordVisibilityToggleEnabled = true
                til_password.error = getString(R.string.password_invalid)
                tiet_confirm_password.setText("")
                tiet_password.requestFocus()
            } else if (tiet_confirm_password.text.toString().trim().isEmpty()) {
                til_confirm_password.error = getString(R.string.reenter_password)
            } else if (tiet_password.text.toString().trim() != tiet_confirm_password.text.toString().trim()) {
                til_confirm_password.isPasswordVisibilityToggleEnabled = true
                til_confirm_password.error = getString(R.string.password_not_matching)
            } else {
                val data = HashMap<String, String>()
                data["password"] = tiet_password.text.toString().trim()
                data["country_code"] = countryCode
                data["phone"] = phone
                mBinding.isLoading = true
                mSignUpViewModel.resetPassword(data)
            }
        }

        mSignUpViewModel.resetPasswordResponseLiveData.removeObservers(this)
        mSignUpViewModel.resetPasswordResponseLiveData.observe(this, Observer { response ->

            if (response == null || response.status == Status.LOADING) {
                return@Observer
            }

            mBinding.isLoading = false

            when {
                response.data == null -> {
                    showSnackBar(response.message!!)
                }

                response.data.status -> {
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

                        }
                    )
                }

                else -> {
                    showSnackBar(response.data.message)
                }
            }

            mSignUpViewModel.resetPassword(null)

        })
    }

    private inner class GenericTextWatcher internal constructor(private val view: View) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            when (view.id) {
                R.id.tiet_password -> til_password.error = null
                R.id.tiet_confirm_password -> til_confirm_password.error = null
            }
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    override fun handleOnBackPressed(): Boolean {
        findNavController().navigate(R.id.action_back_to_verifyPhone)
        return true
    }
}
