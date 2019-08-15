package com.bigtime.ui.home

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import com.bigtime.AppExecutors
import com.bigtime.R
import com.bigtime.common.autoCleared
import com.bigtime.data.api.Status
import com.bigtime.data.model.User
import com.bigtime.databinding.FragmentHomeBinding
import com.bigtime.databinding.ItemUserBinding
import com.bigtime.ui.BaseDataBindListAdapter
import com.bigtime.ui.BaseFragment
import com.bigtime.ui.RetryCallback
import com.bigtime.ui.add_product.AddProductActivity
import com.bigtime.ui.order.OrderActivity
import com.bigtime.utils.SessionUtils
import com.bigtime.widget.CustomDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject


class HomeFragment : BaseFragment<FragmentHomeBinding>(){


    lateinit var mViewModel: HomeViewModel

    @Inject
    lateinit var appExecutors: AppExecutors
    var adapter by autoCleared<ListAdapter>()


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = getViewModel(HomeViewModel::class.java)


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

        //mBinding.image = "https://cdn.freebiesupply.com/logos/large/2x/android-logo-png-transparent.png"

        mBinding.callback = object : RetryCallback {
            override fun retry() {
                mViewModel.retry()



            }
        }

        mBinding.btnAdd.setOnClickListener {
            startActivity(intentFor<AddProductActivity>())
        }

        mBinding.btnOrder.setOnClickListener {
            startActivity(intentFor<OrderActivity>())
        }


        mViewModel.loadData()
    }





    class ListAdapter(
            private val dataBindingComponent: DataBindingComponent,
            appExecutors: AppExecutors,
            private val itemClickCallback: ((User) -> Unit)?
    ) : BaseDataBindListAdapter<User, ItemUserBinding>(
            appExecutors = appExecutors,
            diffCallback = object : DiffUtil.ItemCallback<User>() {
                override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                    return oldItem.id == newItem.id
                            && oldItem.name == newItem.name
                }
            }
    ) {

        override fun createBinding(parent: ViewGroup): ItemUserBinding {
            val binding = DataBindingUtil.inflate<ItemUserBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.item_user,
                    parent,
                    false,
                    dataBindingComponent
            )
            binding.root.setOnClickListener {
                binding.user?.let {
                    itemClickCallback?.invoke(it)
                }
            }
            return binding
        }

        override fun bind(binding: ItemUserBinding, item: User) {
            binding.user = item
        }
    }
}

