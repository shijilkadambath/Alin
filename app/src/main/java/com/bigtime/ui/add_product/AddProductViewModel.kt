package com.bigtime.ui.add_product
/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bigtime.R
import com.bigtime.data.api.BaseResponseThree
import com.bigtime.data.api.Resource
import com.bigtime.data.model.Brand
import com.bigtime.data.model.product_details.*
import com.bigtime.repo.UMSRepository
import javax.inject.Inject
import com.bigtime.utils.AddProductConstants


class AddProductViewModel
@Inject constructor( val repoRepository: UMSRepository) : ViewModel() {

    private var headerIconChange = MutableLiveData<String>()
    var whichFragment: String = AddProductConstants.chooseFragment

    private var brandItem : Brand? = null

    private var lotValueValid = MutableLiveData<Boolean>()
    private var moqValueObserve = MutableLiveData<String>()
    private var moqValueValid = MutableLiveData<Boolean>()
    private var nextButtonEnable = MutableLiveData<Boolean>()

    var lotValue: String = ""
    var moqValue: String = ""


    fun setBrandItem(brandItem: Brand?) {
        this.brandItem = brandItem
    }

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

    /**
     * detail page
     */

    private var observeTypeItems = MutableLiveData<ArrayList<FootwearTypeItem?>>()
    private var observeUpperItems = MutableLiveData<ArrayList<DataItem?>>()
    private var observeInSockItems = MutableLiveData<ArrayList<DataItem?>>()
    private var observeLiningItems = MutableLiveData<ArrayList<DataItem?>>()
    private var observeHeelItems = MutableLiveData<ArrayList<DataItem?>>()

    private val footWearType = ArrayList<FootwearTypeItem?>()

    private var radioGroupId: String? = null

    fun loadBrandDetails(): LiveData<Resource<BaseResponseThree<ArrayList<CategoryItem>, ArrayList<SolesItem>, ArrayList<FootwearTypeItem>>>> {
        val params = HashMap<String, String>()
        params["brandId"] = brandItem?.brandId.toString()
        return repoRepository.loadBrandDetails(params)
    }

    fun processFootWearData(footWears: ArrayList<FootwearTypeItem>) {
        val footwearItem = FootwearTypeItem().apply {
            ID = -1
            type = "Choose Type"
            /*Material = ArrayList<MaterialItem>().apply {
                MaterialItem().apply {
                    name = ""
                }
            }*/
        }
        this.footWearType.clear()
        this.footWearType.add(footwearItem)
        this.footWearType.addAll(footWears)
        observeTypeItems.value = footWearType
    }

    private val upperList =  ArrayList<DataItem?>()
    private val inSockList =  ArrayList<DataItem?>()
    private val liningList =  ArrayList<DataItem?>()
    private val heelList =  ArrayList<DataItem?>()

    fun chooseType(typeId: Int) {
        val materialList = ArrayList<MaterialItem?>()

        when (typeId) {
            -1 -> {
                upperList.clear()
                upperList.add(
                        DataItem().apply {
                            Material = getDefaultValueUpper()
                        }
                )
                inSockList.clear()
                inSockList.add(DataItem().apply {
                    Material = getDefaultValueInSock()
                })
                liningList.clear()
                liningList.add(DataItem().apply {
                    Material =getDefaultValueLining()
                })
                heelList.clear()
                heelList.add(DataItem().apply {
                    Material = getDefaultValueHeel()
                })

                observeUpperItems.value = upperList
                observeInSockItems.value = inSockList
                observeLiningItems.value = liningList
                observeHeelItems.value = heelList

                return
            }
            else -> {
                for (i in 0 until footWearType.size) {
                    if (footWearType[i]?.ID == typeId) {
                        footWearType[i]?.material?.let {
                            materialList.addAll(it) }
                        break
                    }
                }
            }
        }
        materialList.let {
            for (k in 0 until materialList.size) {
                when(materialList[k]?.name) {
                    "Upper" -> {
                        materialList[k]?.data?.let { it1 ->
                            upperList.clear()
                            val dataItem = DataItem().apply {
                                Material = getDefaultValueUpper()
                            }
                            upperList.add(dataItem)
                            upperList.addAll(it1) }
                    }

                    "Insock" -> {
                        materialList[k]?.data?.let { it1 ->
                            inSockList.clear()
                            val dataItem = DataItem().apply {
                                Material = getDefaultValueInSock()
                            }
                            inSockList.add(dataItem)
                            inSockList.addAll(it1) }
                    }

                    "Lining" -> {
                        materialList[k]?.data?.let { it1 ->
                            liningList.clear()
                            val dataItem = DataItem().apply {
                                Material = getDefaultValueLining()
                            }
                            liningList.add(dataItem)
                            liningList.addAll(it1) }
                    }

                    "Heel" -> {
                        materialList[k]?.data?.let { it1 ->
                            heelList.clear()
                            val dataItem = DataItem().apply {
                                Material = getDefaultValueHeel()
                            }
                            heelList.add(dataItem)
                            heelList.addAll(it1) }
                    }
                }
            }

            observeUpperItems.value = upperList
            observeInSockItems.value = inSockList
            observeLiningItems.value = liningList
            observeHeelItems.value = heelList
        }


    }

    fun getTypeData() : LiveData<ArrayList<FootwearTypeItem?>> {
        return observeTypeItems
    }

    fun getUpperData() : LiveData<ArrayList<DataItem?>> {
        return observeUpperItems
    }

    fun getInsockData() : LiveData<ArrayList<DataItem?>> {
        return observeInSockItems
    }

    fun getLiningData() : LiveData<ArrayList<DataItem?>> {
        return observeLiningItems
    }

    fun getHeelData() : LiveData<ArrayList<DataItem?>> {
        return observeHeelItems
    }
    private fun getDefaultValueUpper() : String {
        return "Choose Upper"
    }

    private fun getDefaultValueInSock() : String {
        return "Choose Insock"
    }

    private fun getDefaultValueLining() : String {
        return "Choose Lining"
    }

    private fun getDefaultValueHeel() : String {
        return "Choose Heel"
    }

    fun setRadioGroupId(id: Int) {
        //TODO radiogroup value to api call
        when(id) {
            R.id.radioBtnBranded -> {
                radioGroupId = "branded"
            }

            R.id.radioBtnNonBranded -> {
                radioGroupId = "nonbranded"
            }

            R.id.radioBtnLoosePack -> {
                radioGroupId = "loosePack"
            }

        }
    }

    private val observeBulkMOQ =  MutableLiveData<ArrayList<String?>>()
    private val bulkMOQValues = ArrayList<String?>()

    fun createBulkMOQValues() {
        val z = moqValue.toInt()/lotValue.toInt()
        val limit =  if (z > 20) {
            20
        }else {
            z
        }
        bulkMOQValues.clear()
        bulkMOQValues.add("Choose Value")
        var tempValue = 0
        for (i in 0 until limit) {
            tempValue += lotValue.toInt()
            bulkMOQValues.add(tempValue.toString())
        }
        observeBulkMOQ.value = bulkMOQValues
    }

    fun getBulkMOQValues() : LiveData<ArrayList<String?>> {
        return observeBulkMOQ
    }

}

