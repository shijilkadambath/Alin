package com.smb.smbapplication.ui.login

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.smb.smbapplication.AppExecutors

import com.smb.smbapplication.R
import com.smb.smbapplication.common.autoCleared
import com.smb.smbapplication.databinding.FragmentLoginBinding
import com.smb.smbapplication.ui.BaseFragment
import com.smb.smbapplication.ui.RetryCallback
import javax.inject.Inject

private const val TAG: String = "LoginFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : BaseFragment<FragmentLoginBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var mViewModel: LoginViewModel

    var adapter by autoCleared<ListAdapter>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_login;
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(LoginViewModel::class.java)

        adapter = ListAdapter(dataBindingComponent = dataBindingComponent, appExecutors = appExecutors) {


            navController().navigate(
                    LoginFragmentDirections.showRegistration()
            )
        }
        mViewModel.repositories.observe(this, Observer { result ->

            mBinding.searchResource = result

            mBinding.resultCount = result?.data?.data?.size ?: 0
            adapter.submitList(result?.data?.data)
        })

        mBinding.listUser.adapter = adapter
        //adapter = rvAdapter

        mBinding.image = "https://cdn.freebiesupply.com/logos/large/2x/android-logo-png-transparent.png"

        mBinding.callback = object : RetryCallback {
            override fun retry() {
                mViewModel.retry()



            }
        }


        mViewModel.loadData()



    }

    fun navController() = findNavController()
}
