package com.bigtime.ui.add_product


import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import com.bigtime.R
import com.bigtime.ui.BaseActivity
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.contains
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.bigtime.data.model.Brand
import com.bigtime.utils.AddProductConstants
import kotlinx.android.synthetic.main.activity_add_prodcut.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject


class AddProductActivity : BaseActivity() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AddProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prodcut)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Add Product"

        viewModel = ViewModelProviders.of(this, mViewModelFactory).get(AddProductViewModel::class.java)

        if (intent.hasExtra("brand")) {
            viewModel.setBrandItem(intent.getParcelableExtra("brand"))
        }

        observeDatas()

        setBtnAlpha(false)
        btnNext.setOnClickListener {
            onNextClick()
        }
    }

    private fun onNextClick() {

        when(viewModel.whichFragment) {

            AddProductConstants.chooseFragment -> {

                findNavController(R.id.container).navigate(R.id.AddProductDetailFragment)
            }

            AddProductConstants.detailFragment -> {
                findNavController(R.id.container).navigate(R.id.AddProductUploadFragment)
            }

            AddProductConstants.uploadFragment -> {
                findNavController(R.id.container).navigate(R.id.AddProductConfirmFragment)
            }
        }
    }

    private fun observeDatas() {
        viewModel.getIconChange().observe(this, Observer {
            changeIcons(it)

        })

        viewModel.isNextButtonEnable().observe(this, Observer {
           setBtnAlpha(it)
            btnNext.isEnabled = it
        })
    }

    private fun setBtnAlpha(enable: Boolean) {
        btnNext.alpha = if (enable) {
            1f
        }else {
            0.5f
        }
    }

    private fun changeIcons(value: String) {
        when(value) {
            AddProductConstants.chooseFragment -> {
                v1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                v2.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                chooseIcon.setImageResource(R.drawable.ic_choose_product_title) // blue
                detailIcon.setImageResource(R.drawable.ic_product_details_title) // light
                uploadIcon.setImageResource(R.drawable.ic_product_upload_title) // light

                choose.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                details.setTextColor(ContextCompat.getColor(this, R.color.activeText))
                upload.setTextColor(ContextCompat.getColor(this, R.color.activeText))
            }

            AddProductConstants.detailFragment -> {
                v1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                v2.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                chooseIcon.setImageResource(R.drawable.ic_choose_product_title) // blue
                detailIcon.setImageResource(R.drawable.ic_product_details_title_hover) // blue
                uploadIcon.setImageResource(R.drawable.ic_product_upload_title) // light

                choose.setTextColor(ContextCompat.getColor(this, R.color.activeText))
                details.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                upload.setTextColor(ContextCompat.getColor(this, R.color.activeText))
            }

            AddProductConstants.uploadFragment -> {
                v1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                v2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                chooseIcon.setImageResource(R.drawable.ic_choose_product_title) // blue
                detailIcon.setImageResource(R.drawable.ic_product_details_title_hover) // blue
                uploadIcon.setImageResource(R.drawable.ic_product_upload_title_hover) // blue

                choose.setTextColor(ContextCompat.getColor(this, R.color.activeText))
                details.setTextColor(ContextCompat.getColor(this, R.color.activeText))
                upload.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) {
            onBackPressed()
        }

        return false
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(
                            Context.INPUT_METHOD_SERVICE
                    ) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }
}
