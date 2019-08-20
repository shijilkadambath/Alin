package com.bigtime.ui.add_product


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bigtime.AppExecutors
import com.bigtime.R
import com.bigtime.common.autoCleared
import com.bigtime.data.api.Status
import com.bigtime.databinding.FragmentChooseBrandDialogBinding
import com.bigtime.ui.BaseDialogFragment
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
class ChooseBrandDialogFragment : BaseDialogFragment<FragmentChooseBrandDialogBinding>() {

    private lateinit var viewModel: ChooseBrandViewModel

    private var adapter by autoCleared<BrandsAdapter>()

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = getViewModel(ChooseBrandViewModel::class.java)
        mBinding.layoutBinder = this
        mBinding.recyclerViewBrands.layoutManager = LinearLayoutManager(context)

        mBinding.closeButton.setOnClickListener {
            dismiss()
        }

        adapter = BrandsAdapter(dataBindingComponent, appExecutors = appExecutors){

        }

        mBinding.recyclerViewBrands.adapter = adapter

        val params = HashMap<String, String>()

        params["sessionToken"] = "IlNLMTQ5MDI3MTA2NDE1NjYwNTUwNjUi:1hz0Se:SpzvNCcH2GsDdJSVukbZhZiJb3U"
        params["platform"] = "postman"
        params["packageName"] = "com.bizcrum.shoekonnect"
        params["isAuthRequired"] = "true"
        params["Content-Type"] = "application/json"

        viewModel.loadBrands(params)

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

                    adapter.submitList(response.data.data?.brandList)
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
}
