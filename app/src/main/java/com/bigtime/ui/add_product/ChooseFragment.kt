package com.bigtime.ui.add_product

/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */
import android.os.Bundle
import android.text.*
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigtime.AppExecutors

import com.bigtime.R
import com.bigtime.databinding.FragmentChooseProductBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.utils.AddProductConstants
import javax.inject.Inject
import android.widget.RadioGroup
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.widget.CompoundButton
import android.widget.RadioButton


private const val TAG: String = "LoginFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class ChooseFragment : BaseFragment<FragmentChooseProductBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors
    lateinit var mViewModel: AddProductViewModel


    override fun getLayoutId(): Int {
        return R.layout.fragment_choose_product
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        setUpUI(mBinding.parentView)

        activity?.let {
            mViewModel = getViewModelShared(it, AddProductViewModel::class.java)
        }

        mViewModel.setIconChange(AddProductConstants.chooseFragment)

        initUi()

        /*mBinding.btnNext.setOnClickListener {
            navController().navigate(
                    ChooseFragmentDirections.actionAddProductDetailFragment()
            )

        }*/
    }

    private fun initUi() {

        mBinding.lotValue.addTextChangedListener(MyTextWatcher(mBinding.lotValue))
        mBinding.moqValue.addTextChangedListener(MyTextWatcher(mBinding.moqValue))

        if (mViewModel.lotValue.isEmpty())
            mBinding.lotValue.setText("8")

        /*if (mViewModel.moqValue.isEmpty())
            mBinding.moqValue.setText("8")*/

        mBinding.lotIncrement.setOnClickListener {
            dismissKeyboard(mBinding.lotIncrement.windowToken)
            val lot = mBinding.lotValue.text.toString()
            if (lot.isEmpty()) {
                mBinding.lotValue.setText("3")
                return@setOnClickListener
            }

            val addedValue = (lot.toInt() + 1).toString()
            mBinding.lotValue.setText(addedValue)
        }

        mBinding.lotDecrement.setOnClickListener {
            dismissKeyboard(mBinding.lotDecrement.windowToken)

            val lot = mBinding.lotValue.text.toString()
            if (lot.isEmpty())
                return@setOnClickListener

            val subValue = (lot.toInt() - 1)
            if (subValue >= 0) {
                mBinding.lotValue.setText(subValue.toString())
            }


        }

        mViewModel.isLotValueValid().observe(this, Observer {
            moqViewChange(it)
            mBinding.lotError.visibility = if (it) {
                View.INVISIBLE
            }else{
                View.VISIBLE
            }
        })


        //moqValue
        mBinding.moqIncrement.setOnClickListener {
            dismissKeyboard(mBinding.moqIncrement.windowToken)
            mViewModel.moqValueIncrement(mBinding.moqValue.text.toString())
        }

        mBinding.moqDecrement.setOnClickListener {
            dismissKeyboard(mBinding.moqDecrement.windowToken)
            mViewModel.moqValueDecrement(mBinding.moqValue.text.toString())
        }

        mViewModel.getMoqValue().observe(this, Observer {
//            mBinding.moqValue.setText(if (it.isNullOrEmpty()) "8" else it)
            mBinding.moqValue.setText(it)
        })

        mViewModel.isMoqValueValid().observe(this, Observer {
            mViewModel.enableNextButton(AddProductConstants.chooseFragment)
            mBinding.moqError.visibility = if (it) {
                View.INVISIBLE
            }else{
                View.VISIBLE
            }
        })

        mBinding.radioBtnMen.setOnCheckedChangeListener { radio, checked->

            onCategoryChanged(radio, checked)
        }

        mBinding.radioBtnWomen.setOnCheckedChangeListener { radio, checked->

            onCategoryChanged(radio, checked)
        }

        mBinding.radioBtnKids.setOnCheckedChangeListener { radio, checked->

            onCategoryChanged(radio, checked)
        }

    }

    private fun onCategoryChanged(radioGroup: CompoundButton, checked: Boolean) {

        if (checked){

            when(radioGroup) {
                mBinding.radioBtnMen -> {
                    mViewModel.setCategory(AddProductConstants.categoryMen)
                }

                mBinding.radioBtnWomen -> {
                    mViewModel.setCategory(AddProductConstants.categoryWomen)
                }

                mBinding.radioBtnKids -> {
                    mViewModel.setCategory(AddProductConstants.categoryKids)
                }
            }
            mViewModel.enableNextButton(AddProductConstants.chooseFragment)

            mBinding.radioBtnMen.isChecked = false
            mBinding.radioBtnWomen.isChecked = false
            mBinding.radioBtnKids.isChecked = false
            radioGroup.isChecked = true
        }

    }

    private fun moqViewChange(enabled: Boolean) {
//        mBinding.moqValue.setText("")
        mBinding.moqIncrement.isEnabled = enabled
        mBinding.moqDecrement.isEnabled = enabled
        mBinding.moqValue.isEnabled = enabled
    }

   /* private inner class MyFilter(val sValue: Int, val eValue: Int): InputFilter {

        override fun filter(p0: CharSequence?, p1: Int, p2: Int, p3: Spanned?, p4: Int, p5: Int): CharSequence {
            val input = (p3.toString() + p0.toString()).toInt()
            mViewModel.isInRange(sValue, eValue, input)
            return  ""
        }



    }*/

    private inner class MyTextWatcher(val view: View) : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            when(view.id) {
                R.id.lotValue -> {
                    mViewModel.checkLotValue(p0.toString())
                }

                R.id.moqValue -> {
                    mViewModel.checkMoqValue(p0.toString())
                }
            }
        }

    }

    fun navController() = findNavController()
}
