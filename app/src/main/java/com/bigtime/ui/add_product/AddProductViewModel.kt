package com.bigtime.ui.add_product
/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bigtime.data.api.BaseResponseThree
import com.bigtime.data.api.Resource
import com.bigtime.data.model.product_details.*
import com.bigtime.repo.UMSRepository
import javax.inject.Inject
import com.bigtime.utils.AddProductConstants
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R
import android.util.Base64
import com.bigtime.data.model.*
import java.io.ByteArrayOutputStream


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

    private var category = ""

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

            /*if (moqValue.isNotEmpty()) {
                moqValue = ""
                moqValueObserve.value = ""
                moqValueValid.value = false
            }*/

            lotValueValid.value = (tempValue in 3..24).apply {
                lotValue = if (this) {
                    tempValue.toString()
                } else ""
                moqValueIncrement("0")
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
        return  lotValue.isNotEmpty() && moq/lotValue.toInt() <= 50 && moq%lotValue.toInt() == 0
    }

    fun isMoqValueValid() : LiveData<Boolean> {
        return moqValueValid
    }

    fun enableNextButton(whicView: String) {
        when(whicView) {
            AddProductConstants.chooseFragment -> {
                nextButtonEnable.value = lotValue.isNotEmpty() && moqValue.isNotEmpty() && category.isNotEmpty()
            }
            AddProductConstants.detailFragment -> {
                nextButtonEnable.value = mSkPrice.isNotEmpty()
                        && mMrp.isNotEmpty()
                        && checkBulk()
                        && mPackageType.isNotEmpty()
                        && mStyle.isNotEmpty()
                        && mStyle != getDefaultStyle()
                        && mSole.isNotEmpty()
                        && mSole != getDefaultSole()
                        && mType.isNotEmpty()
                        && mType != getDefaultType()
            }
        }

    }

    private fun checkBulk() : Boolean{
        return if (mBulkMoq.isNotEmpty() && mBulkMoq != getDefaultBulkMoq()) {
            mBulkSkPrice.isNotEmpty()
        }else {
            true
        }
    }

    fun isNextButtonEnable(): LiveData<Boolean> {
        return nextButtonEnable
    }

    fun setCategory(whichCat: String) {
        this.category = whichCat
    }

    /**
     * detail page
     */

    private var observeStyles = MutableLiveData<ArrayList<StylesItem?>>()
    private var observeTypeItems = MutableLiveData<ArrayList<FootwearTypeItem?>>()
    private var observeUpperItems = MutableLiveData<ArrayList<DataItem?>>()
    private var observeInSockItems = MutableLiveData<ArrayList<DataItem?>>()
    private var observeLiningItems = MutableLiveData<ArrayList<DataItem?>>()
    private var observeHeelItems = MutableLiveData<ArrayList<DataItem?>>()

    private val footWearType = ArrayList<FootwearTypeItem?>()

    fun loadBrandDetails(): LiveData<Resource<BaseResponseThree<ArrayList<CategoryItem>, ArrayList<SolesItem>, ArrayList<FootwearTypeItem>>>> {
        val params = HashMap<String, String>()
        params["brandId"] = brandItem?.brandId.toString()
        return repoRepository.loadBrandDetails(params)
    }

    fun processStyles(categories: ArrayList<CategoryItem>) {
        for (i in 0 until categories.size) {
            if (categories[i].Category.equals(category)) {
                categories[i].subCategory.let {
                    val list = ArrayList<StylesItem?>()
                    val stylesItem = StylesItem().apply {
                        ID = -1
                        Style = getDefaultStyle()
                    }
                    list.add(stylesItem)
                    it?.get(0)?.styles?.let { it1 -> list.addAll(it1) }
                    observeStyles.value = list
                }
                break
            }
        }
    }

    private fun getDefaultStyle() : String {
        return "Choose Style"
    }

    private fun getDefaultSole() : String {
        return "Choose Sole"
    }

    private fun getDefaultType() : String {
        return "Choose Type"
    }

    fun processFootWearData(footWears: ArrayList<FootwearTypeItem>) {
        val footwearItem = FootwearTypeItem().apply {
            ID = -1
            type = getDefaultType()
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
        setType(typeId.toString())
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

    fun getStylesData() : LiveData<ArrayList<StylesItem?>> {
        return observeStyles
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
        bulkMOQValues.add(getDefaultBulkMoq())
        var tempValue = 0
        for (i in 0 until limit) {
            tempValue += lotValue.toInt()
            bulkMOQValues.add(tempValue.toString())
        }
        observeBulkMOQ.value = bulkMOQValues
    }

    private fun getDefaultBulkMoq() : String {
        return "Choose Value"
    }

    fun getBulkMOQValues() : LiveData<ArrayList<String?>> {
        return observeBulkMOQ
    }

    private var mSkPrice = ""
    private var mMrp = ""
    private var mBulkMoq = ""
    private var mBulkSkPrice = ""
    private var mPackageType = ""
    private var mStyle = ""
    private var mType = ""
    private var mSole = ""
    private var mLining = ""
    private var mUpperMat = ""
    private var mInSock = ""
    private var mHeel = ""

    fun setSkPrice(value: String) {
        this.mSkPrice = value
        enableNextButton(AddProductConstants.detailFragment)
    }

    fun setMRP(value : String) {
        this.mMrp = value
        enableNextButton(AddProductConstants.detailFragment)
    }

    fun setBulkMOQ(value : String) {
        this.mBulkMoq = value
        enableNextButton(AddProductConstants.detailFragment)
    }

    fun setBulkSKPrice(value : String) {
        this.mBulkSkPrice = value
        enableNextButton(AddProductConstants.detailFragment)
    }

    fun setPackageType(id : Int) {

        //TODO radiogroup value to api call
        when(id) {
            com.bigtime.R.id.radioBtnBranded -> {
                this.mPackageType = "branded"
            }

            com.bigtime.R.id.radioBtnNonBranded -> {
                this.mPackageType = "nonbranded"
            }

            com.bigtime.R.id.radioBtnLoosePack -> {
                this.mPackageType = "loosePack"
            }

        }
        enableNextButton(AddProductConstants.detailFragment)


    }

    fun setStyle(value : String) {
        this.mStyle = value
        enableNextButton(AddProductConstants.detailFragment)
    }

    fun setType(value : String) {
        this.mType = value
        enableNextButton(AddProductConstants.detailFragment)
    }

    fun setSole(value : String) {
        this.mSole = value
        enableNextButton(AddProductConstants.detailFragment)
    }

    fun setLining(value : String) {
        this.mLining = value
        enableNextButton(AddProductConstants.detailFragment)
    }

    fun setUpperMaterial(value : String) {
        this.mUpperMat = value
        enableNextButton(AddProductConstants.detailFragment)
    }

    fun setInSock(value : String) {
        this.mInSock = value
        enableNextButton(AddProductConstants.detailFragment)
    }

    fun setHeel(value : String) {
        this.mHeel = value
        enableNextButton(AddProductConstants.detailFragment)
    }

    /**
     * upload image
     */

    var colorList = ArrayList<ColorListItem?>()

    var imageList = ArrayList<DataImage>()
    val observeImageAdded = MutableLiveData<ArrayList<DataImage>>()

    fun loadColors(): LiveData<Resource<ColorData>> {
        return repoRepository.loadColors()
    }

    fun setColors(list : ArrayList<ColorListItem?>) {
        this.colorList = list
    }

    fun uploadImageToS3(imagePath: String) : LiveData<Resource<DataImageUpload>> {
        val base64Image = createBase64Image(imagePath)
        val params = HashMap<String, String>()
        params["img"] = base64Image
        params["isThumb"] = "0"

        return repoRepository.uploadImageToS3(params)
    }

    private fun createBase64Image(imagePath: String) : String{
        val bm = BitmapFactory.decodeFile(imagePath)
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) //bm is the bitmap object
        val b = baos.toByteArray()
       return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun addImage(imageUrl: String) {
        imageList.add(DataImage(imageUrl))
        observeImageAdded.value = imageList
    }

    fun getImages() : LiveData<ArrayList<DataImage>> {
        return observeImageAdded
    }

}

