package com.bigtime.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigtime.R
import com.bigtime.data.api.Status
import com.bigtime.databinding.FragmentResetPasswordBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.utils.CommonUtils
import com.bigtime.widget.CustomDialog



class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>(), OnBackPressedCallback {

    companion object {
        const val TAG: String = "ResetPasswordFragment"
    }

    private lateinit var mSignUpViewModel: LoginViewModel

    override fun getLayoutId(): Int {
        return R.layout.fragment_reset_password
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) return

        mSignUpViewModel = getViewModel(LoginViewModel::class.java)
        mBinding.layoutBinder = this

        requireActivity().onBackPressedDispatcher.addCallback(this, this)

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mBinding.toolbar.setNavigationOnClickListener { handleOnBackPressed() }

        val countryCode = ResetPasswordFragmentArgs.fromBundle(arguments!!).countryCode
        val phone = ResetPasswordFragmentArgs.fromBundle(arguments!!).phone

        mBinding.tilPassword.typeface = CommonUtils.FONT_METROPOLIS_REGULAR(activity!!)
        mBinding.tilConfirmPassword.typeface = CommonUtils.FONT_METROPOLIS_REGULAR(activity!!)

        mBinding.btnSubmit.setOnClickListener {

            mBinding.tietPassword.addTextChangedListener(GenericTextWatcher(mBinding.tietPassword))
            mBinding.tietConfirmPassword.addTextChangedListener(GenericTextWatcher(mBinding.tietConfirmPassword))

            if (mBinding.tietPassword.text.toString().trim().isEmpty()) {
                mBinding.tilPassword.error = getString(R.string.password_required)
            } else if (!CommonUtils.PASSWORD.matcher(mBinding.tietPassword.text.toString().trim()).matches()) {
                mBinding.tilPassword.isPasswordVisibilityToggleEnabled = true
                mBinding.tilPassword.error = getString(R.string.password_invalid)
                mBinding.tietConfirmPassword.setText("")
                mBinding.tietPassword.requestFocus()
            } else if (mBinding.tietConfirmPassword.text.toString().trim().isEmpty()) {
                mBinding.tilConfirmPassword.error = getString(R.string.reenter_password)
            } else if (mBinding.tietPassword.text.toString().trim() != mBinding.tietConfirmPassword.text.toString().trim()) {
                mBinding.tilConfirmPassword.isPasswordVisibilityToggleEnabled = true
                mBinding.tilConfirmPassword.error = getString(R.string.password_not_matching)
            } else {
                val data = HashMap<String, String>()
                data["password"] = mBinding.tietPassword.text.toString().trim()
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
                R.id.tiet_password -> mBinding.tilPassword.error = null
                R.id.tiet_confirm_password -> mBinding.tilConfirmPassword.error = null
            }
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    override fun handleOnBackPressed(): Boolean {
        findNavController().navigate(R.id.action_back_to_verifyPhone)
        return true
    }
}
