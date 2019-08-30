package com.bigtime.ui.approved_product

/**
 * Created by Shijil Kadambath on 03/08/2018
 *Email : shijilkadambath@gmail.com
 */
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.bigtime.AppExecutors

import com.bigtime.R
import com.bigtime.common.autoCleared
import com.bigtime.data.api.Status
import com.bigtime.data.model.Product
import com.bigtime.databinding.*
import com.bigtime.ui.BaseDataBindListAdapter
import com.bigtime.ui.BaseFragment
import com.bigtime.ui.RetryCallback
import com.bigtime.utils.SessionUtils
import javax.inject.Inject

private const val TAG: String = "LoginFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class ApprovedProductFragment : BaseFragment<FragmentApprovedProductBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors
    lateinit var mViewModel: ApprovedProductViewModel

    lateinit var adapter: ListAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_approved_product
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(ApprovedProductViewModel::class.java)

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mBinding.toolbar.setNavigationOnClickListener {
            activity!!.onBackPressed()
        }
        mBinding.toolbar.title ="Product Listing"

        adapter = ListAdapter(dataBindingComponent = dataBindingComponent, appExecutors = appExecutors) {

            /* navController().navigate(
                     HomeFragmentDirections.showRegistration()
             )*/
        }

        mBinding.recycler.adapter = adapter



        mViewModel.orderResponseLiveData.removeObservers(this)
        mViewModel.orderResponseLiveData.observe(this, Observer { response ->

            if (response == null || response.status == Status.LOADING) {
                return@Observer
            }

            mBinding.isLoading = false

           when {
                response.data == null -> {
                    showSnackBar(response.message!!)
                }


                response.data.status == 1 -> {
                    adapter.submitList(response.data.data)
                    //showSnackBar(response.data.message)
                }

                else -> {
                    showSnackBar(response.data.message)
                }
            }

            mViewModel.loadOrderDetails(null)

        })



        mBinding.isLoading = true
        val data = HashMap<String, String>()
        data["searchKey"] = ""
        data["start"] = "0"
        data["count"] = "25"


        mViewModel.loadOrderDetails(data)
    }

    fun navController() = findNavController()

    class ListAdapter(
            private val dataBindingComponent: DataBindingComponent,
            appExecutors: AppExecutors,
            private val itemClickCallback: ((Product) -> Unit)?
    ) : BaseDataBindListAdapter<Product, ItemProductBinding>(
            appExecutors = appExecutors,
            diffCallback = object : DiffUtil.ItemCallback<Product>() {
                override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                    return oldItem.productID == newItem.productID
                }

                override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                    return oldItem.productID == newItem.productID
                }
            }
    ) {

        override fun createBinding(parent: ViewGroup): ItemProductBinding {
            val binding = DataBindingUtil.inflate<ItemProductBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_product,
                    parent,
                    false,
                    dataBindingComponent
            )
            binding.root.setOnClickListener {
                binding.order?.let {
                    itemClickCallback?.invoke(it)
                }
            }
            return binding
        }

        override fun bind(binding: ItemProductBinding, item: Product) {
            binding.order = item

            binding.tvLanguage.setText(item.brandName)
        }
    }




    class SubListAdapter(
            private val dataBindingComponent: DataBindingComponent,
            appExecutors: AppExecutors,
            private val itemClickCallback: ((Product) -> Unit)?
    ) : BaseDataBindListAdapter<Product, ItemProductBinding>(
            appExecutors = appExecutors,
            diffCallback = object : DiffUtil.ItemCallback<Product>() {
                override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                    return oldItem.productID == newItem.productID
                }

                override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                    return oldItem.productID == newItem.productID
                }
            }
    ) {

        override fun createBinding(parent: ViewGroup): ItemProductBinding {
            val binding = DataBindingUtil.inflate<ItemProductBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_product,
                    parent,
                    false,
                    dataBindingComponent
            )
            binding.root.setOnClickListener {
                binding.order?.let {
                    itemClickCallback?.invoke(it)
                }
            }
            return binding
        }

        override fun bind(binding: ItemProductBinding, item: Product) {
            binding.order = item

            binding.tvLanguage.setText(item.brandName)
        }
    }
}
