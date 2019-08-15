package com.bigtime.ui.pending_product

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
import com.bigtime.databinding.FragmentPendingProductBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.ui.RetryCallback
import javax.inject.Inject

private const val TAG: String = "AddProductDetailFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class PendingProductFragment : BaseFragment<FragmentPendingProductBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors
    lateinit var mViewModel: PendingProductViewModel


    override fun getLayoutId(): Int {
        return R.layout.fragment_pending_product;
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(PendingProductViewModel::class.java)

    }

    fun navController() = findNavController()
}
