package com.bigtime.ui.add_product

/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.RadioGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigtime.AppExecutors

import com.bigtime.R
import com.bigtime.common.autoCleared
import com.bigtime.data.model.product_details.DataItem
import com.bigtime.data.model.product_details.FootwearTypeItem
import com.bigtime.data.model.product_details.SolesItem
import com.bigtime.data.model.product_details.StylesItem
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

    private var adapterSoles by autoCleared<SolesAdapter>()
    private var adapterType by autoCleared<TypeAdapter>()
    private var adapterUpper by autoCleared<CommonSpinnerAdapter>()
    private var adapterInSock by autoCleared<CommonSpinnerAdapter>()
    private var adapterLining by autoCleared<CommonSpinnerAdapter>()
    private var adapterHeel by autoCleared<CommonSpinnerAdapter>()
    private var adapterStyles by autoCleared<StyleAdapter>()

    private var adapterBulkValues by autoCleared<BulkValueAdapter>()


    override fun getLayoutId(): Int {
        return R.layout.fragment_add_product_details
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            mViewModel = getViewModelShared(it, AddProductViewModel::class.java)
        }

        initUi()
        observeDatas()

    }

    private fun initUi() {

        mBinding.skValue.addTextChangedListener(MyTextWatcher(mBinding.skValue))
        mBinding.mrpValue.addTextChangedListener(MyTextWatcher(mBinding.mrpValue))
        mBinding.skValueBulk.addTextChangedListener(MyTextWatcher(mBinding.skValueBulk))

        mViewModel.setIconChange(AddProductConstants.detailFragment)
        mViewModel.enableNextButton(AddProductConstants.detailFragment)
        adapterSoles = SolesAdapter {}

        adapterType = TypeAdapter()
        adapterUpper = CommonSpinnerAdapter()
        adapterInSock = CommonSpinnerAdapter()
        adapterHeel = CommonSpinnerAdapter()
        adapterLining = CommonSpinnerAdapter()
        adapterStyles = StyleAdapter()

        adapterBulkValues = BulkValueAdapter()

        mBinding.sole.adapter = adapterSoles
        mBinding.type.adapter = adapterType
        mBinding.upperMaterial.adapter = adapterUpper
        mBinding.inSock.adapter = adapterInSock
        mBinding.lining.adapter = adapterLining
        mBinding.heel.adapter = adapterHeel
        mBinding.moqValue.adapter = adapterBulkValues
        mBinding.style.adapter = adapterStyles

        mViewModel.createBulkMOQValues()

        mBinding.type.onItemSelectedListener = itemSelectedListner
        mBinding.style.onItemSelectedListener = itemSelectedListner
        mBinding.sole.onItemSelectedListener = itemSelectedListner
        mBinding.lining.onItemSelectedListener = itemSelectedListner
        mBinding.upperMaterial.onItemSelectedListener = itemSelectedListner
        mBinding.inSock.onItemSelectedListener = itemSelectedListner
        mBinding.heel.onItemSelectedListener = itemSelectedListner
        mBinding.moqValue.onItemSelectedListener = itemSelectedListner

    }

    private fun observeDatas() {

        mViewModel.loadBrandDetails().observe(this, Observer {

            if (it.data == null)
                return@Observer

            it.data.data?.let {
                mViewModel.processStyles(it)
            }

            adapterSoles.setData(it.data.data2)

            it.data.data3?.let { it1 -> mViewModel.processFootWearData(it1) }

        })

        mViewModel.getStylesData().observe(this, Observer {
            adapterStyles.setData(it)
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

    private val itemSelectedListner: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            when(p0) {
                mBinding.style -> {
                    val selectedItem = p0?.getItemAtPosition(p2) as StylesItem
                    selectedItem.ID?.let { mViewModel.setStyle(it.toString()) }
                }

                mBinding.sole -> {
                    val selectedItem = p0?.getItemAtPosition(p2) as SolesItem
                    selectedItem.ID?.let { mViewModel.setSole(it.toString()) }
                }

                mBinding.type -> {
                    val selectedItem = p0?.getItemAtPosition(p2) as FootwearTypeItem
                    selectedItem.ID?.let { mViewModel.chooseType(it) }
                }

                mBinding.lining -> {
                    val selectedItem = p0?.getItemAtPosition(p2) as DataItem
                    selectedItem.Material?.let { mViewModel.setLining(it) }
                }

                mBinding.upperMaterial -> {
                    val selectedItem = p0?.getItemAtPosition(p2) as DataItem
                    selectedItem.Material?.let { mViewModel.setUpperMaterial(it) }
                }

                mBinding.inSock -> {
                    val selectedItem = p0?.getItemAtPosition(p2) as DataItem
                    selectedItem.Material?.let { mViewModel.setInSock(it) }
                }

                mBinding.heel -> {
                    val selectedItem = p0?.getItemAtPosition(p2) as DataItem
                    selectedItem.Material?.let { mViewModel.setHeel(it) }
                }

                mBinding.moqValue -> {
                    val selectedItem = p0?.getItemAtPosition(p2) as String
                    mViewModel.setBulkMOQ(selectedItem)
                }

            }
        }

    }

    private val radioGroupListener1: RadioGroup.OnCheckedChangeListener =  RadioGroup.OnCheckedChangeListener { p0, p1 ->
        if (p1 != -1) {
            mBinding.radioGroup2.setOnCheckedChangeListener(null)
            mBinding.radioGroup2.clearCheck()
            mBinding.radioGroup2.setOnCheckedChangeListener(radioGroupListener2)
            mViewModel.setPackageType(mBinding.radioGroup1.checkedRadioButtonId)
        }
    }

    private val radioGroupListener2: RadioGroup.OnCheckedChangeListener = RadioGroup.OnCheckedChangeListener { p0, p1 ->
        if (p1 != -1) {
            mBinding.radioGroup1.setOnCheckedChangeListener(null)
            mBinding.radioGroup1.clearCheck()
            mBinding.radioGroup1.setOnCheckedChangeListener(radioGroupListener1)
            mViewModel.setPackageType(mBinding.radioGroup2.checkedRadioButtonId)
        }
    }

    private inner class MyTextWatcher(val view: View) : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            when(view.id) {
                R.id.skValue -> {
                    mViewModel.setSkPrice(p0.toString())
                }

                R.id.mrpValue -> {
                    mViewModel.setMRP(p0.toString())
                }

                R.id.skValueBulk -> {
                    mViewModel.setBulkSKPrice(p0.toString())
                }
            }
        }

    }

    fun navController() = findNavController()
}
