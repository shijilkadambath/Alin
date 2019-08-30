package com.bigtime.ui.add_product

/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigtime.AppExecutors

import com.bigtime.R
import com.bigtime.common.autoCleared
import com.bigtime.data.model.product_details.FootwearTypeItem
import com.bigtime.databinding.FragmentAddProductDetailsBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.utils.AddProductConstants
import javax.inject.Inject

private const val TAG: String = "LoginFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class AddProductDetailFragment : BaseFragment<FragmentAddProductDetailsBinding>() {

    @Inject
    lateinit var appExecutors: AppExecutors
    lateinit var mViewModel: AddProductViewModel

    private var adapter by autoCleared<SolesAdapter>()
    private var adapterType by autoCleared<TypeAdapter>()
    private var adapterUpper by autoCleared<CommonSpinnerAdapter>()
    private var adapterInSock by autoCleared<CommonSpinnerAdapter>()
    private var adapterLining by autoCleared<CommonSpinnerAdapter>()
    private var adapterHeel by autoCleared<CommonSpinnerAdapter>()

    private var adapterBulkValues by autoCleared<BulkValueAdapter>()


    override fun getLayoutId(): Int {
        return R.layout.fragment_add_product_details
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            mViewModel = getViewModelShared(it, AddProductViewModel::class.java)
        }

        mViewModel.setIconChange(AddProductConstants.detailFragment)
        adapter = SolesAdapter {}

        adapterType = TypeAdapter()
        adapterUpper = CommonSpinnerAdapter()
        adapterInSock = CommonSpinnerAdapter()
        adapterHeel = CommonSpinnerAdapter()
        adapterLining = CommonSpinnerAdapter()

        adapterBulkValues = BulkValueAdapter()

        mBinding.sole.adapter = adapter
        mBinding.type.adapter = adapterType
        mBinding.upperMaterial.adapter = adapterUpper
        mBinding.inSock.adapter = adapterInSock
        mBinding.lining.adapter = adapterLining
        mBinding.heel.adapter = adapterHeel
        mBinding.moqValue.adapter = adapterBulkValues

        mViewModel.createBulkMOQValues()

        mBinding.type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem = p0?.getItemAtPosition(p2) as FootwearTypeItem
                selectedItem.ID?.let { mViewModel.chooseType(it) }
            }

        }

        mViewModel.loadBrandDetails().observe(this, Observer {

            if (it.data == null)
                return@Observer

            adapter.setData(it.data.data2)

            it.data.data3?.let { it1 -> mViewModel.processFootWearData(it1) }

        })

        mViewModel.getTypeData().observe(this, Observer {
            adapterType.setData(it)
        })


        mViewModel.getUpperData().observe(this, Observer {
            adapterUpper.setData(it)
        })

        mViewModel.getLiningData().observe(this, Observer {
            adapterLining.setData(it)
        })

        mViewModel.getInsockData().observe(this, Observer {
            adapterInSock.setData(it)
        })

        mViewModel.getHeelData().observe(this, Observer {
            adapterHeel.setData(it)
        })


        mBinding.radioGroup1.clearCheck()
        mBinding.radioGroup2.clearCheck()
        mBinding.radioGroup1.setOnCheckedChangeListener(radioGroupListener1)
        mBinding.radioGroup2.setOnCheckedChangeListener(radioGroupListener2)

        mViewModel.getBulkMOQValues().observe(this, Observer {
            adapterBulkValues.setData(it)
        })
    }

    private val radioGroupListener1: RadioGroup.OnCheckedChangeListener =  RadioGroup.OnCheckedChangeListener { p0, p1 ->
        if (p1 != -1) {
            mBinding.radioGroup2.setOnCheckedChangeListener(null)
            mBinding.radioGroup2.clearCheck()
            mBinding.radioGroup2.setOnCheckedChangeListener(radioGroupListener2)
            mViewModel.setRadioGroupId(mBinding.radioGroup1.checkedRadioButtonId)
        }
    }

    private val radioGroupListener2: RadioGroup.OnCheckedChangeListener = RadioGroup.OnCheckedChangeListener { p0, p1 ->
        if (p1 != -1) {
            mBinding.radioGroup1.setOnCheckedChangeListener(null)
            mBinding.radioGroup1.clearCheck()
            mBinding.radioGroup1.setOnCheckedChangeListener(radioGroupListener1)
        }
    }



//    int chkId1 = rg1.getCheckedRadioButtonId();
//    int chkId2 = rg2.getCheckedRadioButtonId();
//    int realCheck = chkId1 == -1 ? chkId2 : chkId1;

    fun navController() = findNavController()
}
