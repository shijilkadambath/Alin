package com.bigtime.ui.add_product

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigtime.AppExecutors

import com.bigtime.R
import com.bigtime.common.autoCleared
import com.bigtime.databinding.FragmentLoginBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.ui.RetryCallback
import javax.inject.Inject

private const val TAG: String = "LoginFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : BaseFragment<FragmentLoginBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var mViewModel: AddProductViewModel

    var adapter by autoCleared<ListAdapter>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_choose_product;
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(AddProductViewModel::class.java)

        adapter = ListAdapter(dataBindingComponent = dataBindingComponent, appExecutors = appExecutors) {


           /* navController().navigate(
                    HomeFragmentDirections.showRegistration()
            )*/
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
