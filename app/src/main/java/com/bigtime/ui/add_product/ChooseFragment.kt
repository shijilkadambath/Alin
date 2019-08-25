package com.bigtime.ui.add_product

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import android.os.Bundle
import android.text.*
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigtime.AppExecutors

import com.bigtime.R
import com.bigtime.common.autoCleared
import com.bigtime.databinding.FragmentChooseProductBinding
import com.bigtime.databinding.FragmentLoginBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.ui.RetryCallback
import javax.inject.Inject

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

        activity?.let {
            mViewModel = getViewModelShared(it, AddProductViewModel::class.java)
        }

        mViewModel.setIconChange("frag1")

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

        mViewModel.setDefaultLotValue(8)

        mBinding.lotIncrement.setOnClickListener {
            dismissKeyboard(mBinding.lotIncrement.windowToken)
            mViewModel.lotValueIncrement(mBinding.lotValue.text.toString())
        }

        mBinding.lotDecrement.setOnClickListener {
            dismissKeyboard(mBinding.lotDecrement.windowToken)
            mViewModel.lotValueDecrement(mBinding.lotValue.text.toString())
        }

        mViewModel.getLotValue().observe(this, Observer {value ->
            mBinding.lotValue.setText(value)
        })

        mViewModel.isLotValueSuccess().observe(this, Observer {
            moqViewChange(it)
            if (!it) {
                showSnackBar("Invalid Size")
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
            mBinding.moqValue.setText(it)
        })

        mViewModel.isMoqValueSuccess().observe(this, Observer {
            if (!it) {
                showSnackBar("Invalid MOQ")
            }
        })

    }

    private fun moqViewChange(enabled: Boolean) {
        mBinding.moqValue.setText("")
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
