package com.bigtime.ui.add_product

/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigtime.AppExecutors

import com.bigtime.R
import com.bigtime.common.autoCleared
import com.bigtime.data.api.Status
import com.bigtime.data.model.Product
import com.bigtime.data.model.ProductVariant
import com.bigtime.databinding.*
import com.bigtime.ui.BaseDataBindListAdapter
import com.bigtime.ui.BaseFragment
import com.bigtime.ui.RetryCallback
import com.bigtime.ui.approved_product.ApprovedProductFragment
import javax.inject.Inject

private const val TAG: String = "ConfirmFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class ConfirmFragment : BaseFragment<FragmentConfirmBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors
    lateinit var mViewModel: AddProductViewModel
    override fun getLayoutId(): Int {
        return R.layout.fragment_confirm;
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(AddProductViewModel::class.java)



        mViewModel.productPreviewResponseLiveData.removeObservers(this)
        mViewModel.productPreviewResponseLiveData.observe(this, Observer { response ->

            if (response == null || response.status == Status.LOADING) {
                return@Observer
            }

            mBinding.isLoading = false

            when {
                response.data == null -> {
                    showSnackBar(response.message!!)
                }


                response.data.status == 1 -> {
                    //showSnackBar(response.data.message)
                }

                else -> {
                    showSnackBar(response.data.message)
                }
            }

            mViewModel.loadProductPreview(null)

        })

        mViewModel.loadProductPreview("1075326")

    }



    fun navController() = findNavController()
}
