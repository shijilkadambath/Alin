package com.bigtime.ui

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bigtime.R
import com.bigtime.binding.FragmentDataBindingComponent
import com.bigtime.common.autoCleared
import com.bigtime.data.api.StatusCode
import com.bigtime.di.Injectable
import com.bigtime.ui.home.HomeActivity
import com.bigtime.ui.login.LoginFragment
import com.bigtime.utils.CommonUtils
import com.bigtime.utils.SessionUtils
import com.bigtime.widget.CustomDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import org.jetbrains.anko.support.v4.intentFor
import javax.inject.Inject

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */


/**
 * A generic Fragment   .
 * @param <T> The type of the ViewDataBinding.
*/

abstract class BaseFragment< T : ViewDataBinding> : Fragment() , Injectable {

    private var mActivity: BaseActivity? = null

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @Inject
    protected lateinit var mViewModelFactory: ViewModelProvider.Factory

    var mBinding by autoCleared<T>()

    @LayoutRes
    abstract fun getLayoutId(): Int


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(),
                container, false, dataBindingComponent)

        return mBinding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.mActivity = context
        }
    }


    fun <V : ViewModel> getViewModel(clazz: Class<V>): V {
       return  ViewModelProviders.of(this, mViewModelFactory).get(clazz)

    }

    fun <V : ViewModel> getViewModelShared(activity: FragmentActivity, clazz: Class<V>): V {
        return  ViewModelProviders.of(activity, mViewModelFactory).get(clazz)

    }

    fun showSnackBar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(mBinding.root, message, duration).show()
    }

    fun showActionSnackBar(message: String, firstAction: String = "Retry",
                           iActionSnackBarListener: IActionSnackBarListener,
                           duration: Int = Snackbar.LENGTH_LONG) {
        val actionSnackBar: Snackbar = Snackbar.make(mBinding.root, message, duration)
        actionSnackBar.setAction(firstAction) {
            iActionSnackBarListener.onActionClicked()
        }
        actionSnackBar.setActionTextColor(ContextCompat.getColor(activity!!, R.color.colorAccent))
        actionSnackBar.show()
    }

    interface IActionSnackBarListener {
        fun onActionClicked()
    }

    fun getFCMTokens() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (task.result != null) {
                    val token = task.result!!.token
                    SessionUtils.saveFCMToken(token)
                }
            } else {
                Log.w(LoginFragment.TAG, "Getting FirebaseInstanceId failed", task.exception)
            }
        }
    }


    fun dismissKeyboard(windowToken: IBinder) {
        dismissKeyboard(activity, windowToken)
    }

    private fun dismissKeyboard(activity: FragmentActivity?, windowToken: IBinder) {

        if (activity != null) {
            val imm = activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }

    fun isApiCommonError(statusCode: Int, statusMessage: String): Boolean {
        return when (statusCode) {
            StatusCode.SESSION_EXPIRED -> {
                showAlertSessionExpired(statusMessage)
                true
            }
            StatusCode.IS_ACCOUNT_BLOCKED -> {
                showAlertAccountBlocked(statusMessage)
                true
            }
            else -> false
        }
    }

    private fun showAlertSessionExpired(message: String) {

        CustomDialog.with(
                activity!!,
                R.drawable.ic_close,
                getString(
                        R.string.hello_user,
                        if (SessionUtils.loginSession!!.companyName.isNullOrEmpty()) "" else SessionUtils.loginSession!!.companyName
                ),
                message,
                true,
                getString(R.string.login_to_continue), object : CustomDialog.ICustomDialogListener {
            override fun onActionClicked() {
                SessionUtils.clearSession()
                activity!!.startActivity(intentFor<HomeActivity>())
                activity!!.finishAffinity()
            }

        }
                , false)

    }

    // TODO: This dialog is just a sample. Actions are not implemented.
    fun showAlertAccountBlocked(message: String) {

        val alertDialogBuilder = AlertDialog.Builder(activity!!, R.style.AlertDialogTheme)

        alertDialogBuilder.setTitle("Account Blocked")
        alertDialogBuilder.setMessage(message)

        alertDialogBuilder.setNegativeButton("Send Email") { dialog, which ->

        }

        alertDialogBuilder.setPositiveButton("Call Support") { dialog, which ->

        }

        val dialog = alertDialogBuilder.create()

        dialog.show()
        dialog.setCancelable(false)

        dialog.findViewById<TextView>(android.R.id.message)!!.typeface =
                CommonUtils.FONT_METROPOLIS_REGULAR(activity!!)

    }

}
