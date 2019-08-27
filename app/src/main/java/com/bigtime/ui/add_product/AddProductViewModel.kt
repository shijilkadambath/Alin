package com.bigtime.ui.add_product
/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bigtime.repo.UMSRepository
import javax.inject.Inject
import com.bigtime.utils.AddProductConstants


class AddProductViewModel
@Inject constructor( repoRepository: UMSRepository) : ViewModel() {

    private var headerIconChange = MutableLiveData<String>()
    var whichFragment: String = AddProductConstants.chooseFragment

    private var lotValueValid = MutableLiveData<Boolean>()
    private var moqValueObserve = MutableLiveData<String>()
    private var moqValueValid = MutableLiveData<Boolean>()
    private var nextButtonEnable = MutableLiveData<Boolean>()

    var lotValue: String = ""
    var moqValue: String = ""

    fun getIconChange() : LiveData<String> {
        return headerIconChange
    }

    fun setIconChange(whichView: String) {
        whichFragment = whichView
        headerIconChange.value = whichView
    }

    fun checkLotValue(value: String) {
        if (value.isNotEmpty()) {
            val tempValue = value.toInt()

            if (moqValue.isNotEmpty()) {
                moqValue = ""
                moqValueObserve.value = ""
                moqValueValid.value = false
            }

            lotValueValid.value = (tempValue in 3..24).apply {
                lotValue = if (this) tempValue.toString() else ""
            }
        }else {
            lotValue = ""
        }
    }
    fun isLotValueValid() : LiveData<Boolean> {
        return lotValueValid
    }

    //moqValueLogic
    fun moqValueIncrement(moq: String) {
        if (moq.isNotEmpty()) {
            val tempMoq = moq.toInt()
            if (isMoqMultiplyValid(tempMoq))
            moqValueObserve.value = (tempMoq + lotValue.toInt()).toString()
        }else {
            moqValueObserve.value = lotValue
        }
    }

    fun moqValueDecrement(moq: String) {
        if (moq.isNotEmpty()) {
            val tempMoq = moq.toInt()
            if (isMoqMultiplyValid(tempMoq) && tempMoq > lotValue.toInt())
            moqValueObserve.value = (tempMoq - lotValue.toInt()).toString()
        }else {
            moqValueObserve.value = lotValue
        }
    }

    fun getMoqValue() : LiveData<String> {
        return moqValueObserve
    }

    fun checkMoqValue(moq: String) {
        if (moq.isNotEmpty()) {
            val m = moq.toInt()
            moqValueValid.value = (m in 3..1200 && isMoqMultiplyValid(m)).apply {
                moqValue = if (this) m.toString()  else ""
            }
        }else {
            moqValueValid.value = false
            moqValue = ""
        }
    }

    private fun isMoqMultiplyValid(moq: Int) : Boolean {
        return  moq/lotValue.toInt() <= 50 && moq%lotValue.toInt() == 0
    }

    fun isMoqValueValid() : LiveData<Boolean> {
        return moqValueValid
    }

    fun enableNextButton(enable: Boolean) {
        nextButtonEnable.value = enable
    }

    fun isNextButtonEnable(): LiveData<Boolean> {
        return nextButtonEnable
    }

}

