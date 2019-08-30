package com.bigtime.ui.order

/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bigtime.AppExecutors

import com.bigtime.R
import com.bigtime.common.autoCleared
import com.bigtime.data.api.Status
import com.bigtime.data.model.Order
import com.bigtime.databinding.*
import com.bigtime.ui.BaseDataBindListAdapter
import com.bigtime.ui.BaseFragment
import com.bigtime.ui.RetryCallback
import com.bigtime.utils.SessionUtils
import javax.inject.Inject

private const val TAG: String = "OrderFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class OrderFragment : BaseFragment<FragmentOrderBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors
    lateinit var mViewModel: OrderViewModel

    lateinit var adapter: ListAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_order
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(OrderViewModel::class.java)

        mBinding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mBinding.toolbar.setNavigationOnClickListener {
            activity!!.onBackPressed()
        }
        mBinding.toolbar.title ="My Orders"

        adapter = ListAdapter(dataBindingComponent = dataBindingComponent, appExecutors = appExecutors) {


            /* navController().navigate(
                     HomeFragmentDirections.showRegistration()
             )*/
        }

        mBinding.recycler.layoutManager = GridLayoutManager(activity,2)
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
                    showSnackBar(response.data.message)
                }

                else -> {
                    showSnackBar(response.data.message)
                }
            }

            mViewModel.loadOrderDetails(null)

        })



        var list = ArrayList<Order>()
        list.add(Order("1","New",129,R.drawable.ic_product_new))
        list.add(Order("1","In Process",12,R.drawable.ic_product_progress))
        list.add(Order("1","In Packing",100,R.drawable.ic_product_progress))
        list.add(Order("1","Initiate Pickup",229,R.drawable.ic_product_pickup))
        list.add(Order("1","Dispatch",19,R.drawable.ic_product_dispatch))
        list.add(Order("1","Cancelled",100,R.drawable.ic_product_cancelled))


        adapter.submitList(list)

        val data = HashMap<String, String>()
        data["userID"] = SessionUtils.loginSession!!.userId.toString()
        data["token"] = SessionUtils.getAuthTokens(true)!!

        mViewModel.loadOrderDetails(data)

    }

    fun navController() = findNavController()


    class ListAdapter(
            private val dataBindingComponent: DataBindingComponent,
            appExecutors: AppExecutors,
            private val itemClickCallback: ((Order) -> Unit)?
    ) : BaseDataBindListAdapter<Order, ItemOrderBinding>(
            appExecutors = appExecutors,
            diffCallback = object : DiffUtil.ItemCallback<Order>() {
                override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                    return oldItem.id == newItem.id
                }
            }
    ) {

        override fun createBinding(parent: ViewGroup): ItemOrderBinding {
            val binding = DataBindingUtil.inflate<ItemOrderBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_order,
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

        override fun bind(binding: ItemOrderBinding, item: Order) {
            binding.order = item
        }
    }
}
