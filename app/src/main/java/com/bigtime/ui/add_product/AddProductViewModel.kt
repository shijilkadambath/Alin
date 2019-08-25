package com.bigtime.ui.add_product
/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bigtime.common.AbsentLiveData
import com.bigtime.data.api.BaseResponse
import com.bigtime.data.api.Resource
import com.bigtime.data.model.User
import com.bigtime.repo.UMSRepository
import javax.inject.Inject
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class AddProductViewModel
@Inject constructor( repoRepository: UMSRepository) : ViewModel() {

    private var headerIconChange = MutableLiveData<String>()
    private var lotValueSuccess = MutableLiveData<Boolean>()
    private var lotValueObserve = MutableLiveData<String>()
    private var moqValueObserve = MutableLiveData<String>()
    private var moqValueSuccess = MutableLiveData<Boolean>()

    private var lotValue: Int = 0
    private var moqValue: Int = 0

    fun getIconChange() : LiveData<String> {
        return headerIconChange
    }

    fun setIconChange(whichView: String) {
        headerIconChange.value = whichView
    }

    //lot value logic
    fun setDefaultLotValue(defaultLotValue: Int) {
        lotValueObserve.value = defaultLotValue.toString()
    }
    fun lotValueIncrement(lot: String) {
        lotValueObserve.value = (lot.toInt() + 1).toString()
    }

    fun lotValueDecrement(lot: String) {
        val v = lot.toInt() - 1
        if (v >= 0) {
            lotValueObserve.value = v.toString()
        }
    }

    fun getLotValue(): LiveData<String> {
        return lotValueObserve
    }

    fun checkLotValue(value: String) {
        if (value.isNotEmpty()) {
            val tempValue = value.toInt()

            val length = value.length
            lotValueSuccess.value = (tempValue in 3..24 && length <= 2).apply {
                lotValue = if (this) tempValue else 0
            }
        }
    }
    fun isLotValueSuccess() : LiveData<Boolean> {
        return lotValueSuccess
    }

    //moqValueLogic
    fun moqValueIncrement(moq: String) {
        if (moq.isNotEmpty()) {
            val tempMoq = moq.toInt()
            if (isMoqMultiplyValid(tempMoq))
            moqValueObserve.value = (tempMoq + lotValue).toString()
        }else {
            moqValueObserve.value = lotValue.toString()
        }
    }

    fun moqValueDecrement(moq: String) {
        if (moq.isNotEmpty()) {
            val tempMoq = moq.toInt()
            if (isMoqMultiplyValid(tempMoq) && tempMoq > lotValue)
            moqValueObserve.value = (tempMoq - lotValue).toString()
        }else {
            moqValueObserve.value = lotValue.toString()
        }
    }

    fun getMoqValue() : LiveData<String> {
        return moqValueObserve
    }

    fun checkMoqValue(moq: String) {
        if (moq.isNotEmpty()) {
            val m = moq.toInt()
            moqValueSuccess.value = (m in 3..1200 && isMoqMultiplyValid(m)).apply {
                moqValue = if (this) m  else 0
            }
        }
    }

    private fun isMoqMultiplyValid(moq: Int) : Boolean {
        return  moq/lotValue <= 50
    }

    fun isMoqValueSuccess() : LiveData<Boolean> {
        return moqValueSuccess
    }

}

