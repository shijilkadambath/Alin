package com.bigtime.ui.add_product


import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigtime.AppExecutors
import com.bigtime.R
import com.bigtime.common.autoCleared
import com.bigtime.data.api.Status
import com.bigtime.data.model.Brand
import com.bigtime.databinding.FragmentChooseBrandDialogBinding
import com.bigtime.databinding.FragmentPriceBreakupDialogBinding
import com.bigtime.di.AppModule
import com.bigtime.ui.BaseDialogFragment
import com.bigtime.utils.AppConstants
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChooseBrandDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PriceBreakDialogFragment : BaseDialogFragment<FragmentPriceBreakupDialogBinding>() {

    private lateinit var viewModel: ChooseBrandViewModel

    private var adapter by autoCleared<BrandsAdapter>()
    private var listener by autoCleared<Callback>()

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun getLayoutId(): Int {
        return R.layout.fragment_choose_brand_dialog
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun setCallBack(listener: Callback) {
        this.listener = listener
    }

    override fun onResume() {

        val window = dialog?.window

//        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        window?.requestFeature(Window.FEATURE_NO_TITLE)

        val point  = Point()
        val display: Display? = window?.windowManager?.defaultDisplay
        display?.getSize(point)
        window?.setLayout((point.x * 0.75).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)

        super.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = getViewModel(ChooseBrandViewModel::class.java)

        mBinding.recyclerViewBrands.layoutManager = LinearLayoutManager(context)

        mBinding.closeButton.setOnClickListener {
            dismiss()
        }

        adapter = BrandsAdapter(dataBindingComponent, appExecutors = appExecutors){item ->
           viewModel.selectedItem(item)
        }

        mBinding.nextButton.setOnClickListener {
            if (viewModel.getSelectedBrand() != null) {
                listener.onNextClick(viewModel.getSelectedBrand())
                dismiss()
            }else {
                showSnackBar("Please Select Brand")
            }

        }

        mBinding.recyclerViewBrands.adapter = adapter
        mBinding.showLoading = true




        viewModel.loadBrands()

        viewModel.getBrands.observe(this, Observer { response ->

            if (response == null || response.status == Status.LOADING) {
                return@Observer
            }

            mBinding.showLoading = false

            when {
                response.data == null -> {
                    return@Observer
                }

                response.data.isSuccess() ->{

                    mBinding.searchResource = response
                    adapter.submitList(response.data.data)
                    //adapter.submitList(response.data.data2)  Main category list
                }
                else -> {

                }
            }
        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChooseBrandDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ChooseBrandDialogFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    interface Callback {
        fun onNextClick(brandItem: Brand?)
    }
}
