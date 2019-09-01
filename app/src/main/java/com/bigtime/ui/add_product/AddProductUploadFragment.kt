package com.bigtime.ui.add_product

/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bigtime.AppExecutors

import com.bigtime.R
import com.bigtime.common.autoCleared
import com.bigtime.databinding.FragmentAddProductDetailsBinding
import com.bigtime.databinding.FragmentAddProductUploadBinding
import com.bigtime.databinding.FragmentChooseProductBinding
import com.bigtime.databinding.FragmentLoginBinding
import com.bigtime.ui.BaseFragment
import com.bigtime.ui.RetryCallback
import com.bigtime.utils.FileUtils
import com.shijil.imagepicker.RxImageConverters
import com.shijil.imagepicker.RxImagePicker
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import javax.inject.Inject

private const val TAG: String = "AddProductUploadFragment"

/**
 * A simple [Fragment] subclass.
 *
 */
class AddProductUploadFragment : BaseFragment<FragmentAddProductUploadBinding>() {


    @Inject
    lateinit var appExecutors: AppExecutors
    lateinit var mViewModel: AddProductViewModel


    override fun getLayoutId(): Int {
        return R.layout.fragment_add_product_upload;
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            mViewModel = getViewModelShared(it, AddProductViewModel::class.java)
        }
        mViewModel.setIconChange("frag3")

        mBinding.txtAdd.setOnClickListener{
            RxImagePicker.with(activity).requestImage()
        }

        RxImagePicker.with(activity).requestImageLiveData().observe(this@AddProductUploadFragment,
                Observer { this@AddProductUploadFragment.onImagePicked(it!!) })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            val path = result.uri ?: return


            /*val profileImage = createImageFile(activity!!, Document.PROFILE_PIC)

            FileUtils.overwriteFile(path, profileImage)*/


        }
    }

    fun navController() = findNavController()
    private fun onImagePicked(result: Uri) {
        RxImageConverters.uriToFile(activity, result, FileUtils.createTempImageFile(activity!!))
                .observe(this, object : Observer<File> {
                    override fun onChanged(file: File?) {

                        RxImageConverters.uriToFileLiveData().removeObserver(this)

                        if (file == null) return
                        this@AddProductUploadFragment.startCropImage(file)

                    }
                })
    }
    private fun startCropImage(uri: File?) {
        CropImage.activity(Uri.fromFile(uri))
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .setAutoZoomEnabled(false)
                .start(this)
    }

}
