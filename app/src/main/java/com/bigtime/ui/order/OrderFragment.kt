package com.bigtime.ui.order

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
import com.bigtime.databinding.FragmentOrderBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.ui.RetryCallback
import javax.inject.Inject

private const val TAG: String = "AddProductDetailFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class OrderFragment : BaseFragment<FragmentOrderBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors
    lateinit var mViewModel: OrderViewModel


    override fun getLayoutId(): Int {
        return R.layout.fragment_order;
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(OrderViewModel::class.java)

    }

    fun navController() = findNavController()
}
