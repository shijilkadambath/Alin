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

    lateinit var adapter: ListAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_confirm;
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(AddProductViewModel::class.java)

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mBinding.toolbar.setNavigationOnClickListener {
            activity!!.onBackPressed()
        }
        mBinding.toolbar.title ="Confirm"
    }



    fun navController() = findNavController()
}
