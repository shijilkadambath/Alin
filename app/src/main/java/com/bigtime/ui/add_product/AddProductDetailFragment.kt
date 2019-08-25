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
import com.bigtime.databinding.FragmentAddProductDetailsBinding
import com.bigtime.databinding.FragmentChooseProductBinding
import com.bigtime.databinding.FragmentLoginBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.ui.RetryCallback
import javax.inject.Inject

private const val TAG: String = "LoginFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class AddProductDetailFragment : BaseFragment<FragmentAddProductDetailsBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors
    lateinit var mViewModel: AddProductViewModel


    override fun getLayoutId(): Int {
        return R.layout.fragment_add_product_details;
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            mViewModel = getViewModelShared(it, AddProductViewModel::class.java)
        }

        mViewModel.setIconChange("frag2")

        mBinding.btnNext.setOnClickListener {
            navController().navigate(
                    AddProductDetailFragmentDirections.actionAddProductUploadFragment()
            )

        }
    }

    fun navController() = findNavController()
}
