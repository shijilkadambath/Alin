package com.bigtime.ui.approved_product

/**
 * Created by Shijil Kadambath on 03/08/2018
 *Email : shijilkadambath@gmail.com
 */
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
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
import com.bigtime.utils.SessionUtils
import javax.inject.Inject
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.view.KeyEvent


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
        mBinding.toolbar.title = "Product Listing"

        adapter = ListAdapter(dataBindingComponent = dataBindingComponent, appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.productID == newItem.productID
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.productID == newItem.productID
            }
        })

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



        loadData()

        mBinding.btnSearch.setOnClickListener(View.OnClickListener {

            dismissKeyboard(mBinding.edtSearch.windowToken)
            loadData()

        })

        mBinding.edtSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    dismissKeyboard(mBinding.edtSearch.windowToken)
                    loadData()
                    return true
                }
                return false
            }
        })

    }

    fun loadData(){
        mBinding.isLoading = true
        val data = HashMap<String, String>()
        data["searchKey"] = mBinding.edtSearch.text.toString()
        data["start"] = "0"
        data["count"] = "25"


        mViewModel.loadOrderDetails(data)
    }

    fun navController() = findNavController()


    class ListAdapter(
            private val dataBindingComponent: DataBindingComponent,
            val appExecutors: AppExecutors,
            diffCallback: DiffUtil.ItemCallback<Product>) : androidx.recyclerview.widget.ListAdapter<Product, ListAdapter.DataBindViewHolder>(

            AsyncDifferConfig.Builder<Product>(diffCallback)
                    .setBackgroundThreadExecutor(appExecutors.diskIO())
                    .build()
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.DataBindViewHolder{
            val binding = DataBindingUtil.inflate<ItemProductBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_product,
                    parent,
                    false,
                    dataBindingComponent
            )


            return DataBindViewHolder(binding,dataBindingComponent,appExecutors )
        }

        override fun onBindViewHolder(holder: ListAdapter.DataBindViewHolder, position: Int) {

            val product =getItem(position);
            holder.binding.order = product
            holder.adapter.submitList(getItem(position).variants)
            holder.binding.tvSub.text =product.mainCategory+" > "+product.subCategory
            holder.binding.tvPrice.text ="₹"+product.skPrice
            holder.binding.tvMrp.text ="MRP ₹"+product.skPrice
            holder.binding.executePendingBindings()
        }


       /* class DataBindViewHolder constructor(val binding: ItemProductBinding) :
                RecyclerView.ViewHolder(binding.root)*/

        class DataBindViewHolder(val binding: ItemProductBinding,dataBindingComponent: DataBindingComponent,
                                 appExecutors: AppExecutors) : RecyclerView.ViewHolder(binding.root) {


            val adapter: SubListAdapter

            init {
                adapter =  SubListAdapter(dataBindingComponent = dataBindingComponent, appExecutors = appExecutors)
                binding.recycler.layoutManager = LinearLayoutManager(binding.recycler.context,RecyclerView.HORIZONTAL,false)
                binding.recycler.adapter = adapter
            }
        }

    }





    class SubListAdapter(
            private val dataBindingComponent: DataBindingComponent,
            appExecutors: AppExecutors) : BaseDataBindListAdapter<ProductVariant, ItemProductVariantBinding
            >(
            appExecutors = appExecutors,
            diffCallback = object : DiffUtil.ItemCallback<ProductVariant>() {
                override fun areItemsTheSame(oldItem: ProductVariant, newItem: ProductVariant): Boolean {
                    return oldItem.variantID == newItem.variantID
                }

                override fun areContentsTheSame(oldItem: ProductVariant, newItem: ProductVariant): Boolean {
                    return oldItem.variantID == newItem.variantID
                }
            }
    ) {

        override fun createBinding(parent: ViewGroup): ItemProductVariantBinding {
            val binding = DataBindingUtil.inflate<ItemProductVariantBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_product_variant,
                    parent,
                    false,
                    dataBindingComponent
            )
            return binding
        }

        override fun bind(binding: ItemProductVariantBinding, item: ProductVariant) {
            binding.order = item

            binding.sdvUserPic.setImageURI(
            Uri.parse(item.defImage));


            binding.tvLanguage.text ="Code: "+item.variantID
            binding.switchActive.isChecked = item.isActive ==1
            //binding.tvLanguage.setText(item.brandName)
        }
    }
}
