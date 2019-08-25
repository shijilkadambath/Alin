package com.bigtime.ui.add_product


import android.os.Bundle
import com.bigtime.R
import com.bigtime.ui.BaseActivity
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_add_prodcut.*
import javax.inject.Inject


class AddProductActivity : BaseActivity() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: AddProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_prodcut)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this, mViewModelFactory).get(AddProductViewModel::class.java)

        observeDatas()
    }

    private fun observeDatas() {
        viewModel.getIconChange().observe(this, Observer {
            changeIcons(it)

        })
    }

    private fun changeIcons(value: String) {
        when(value) {
            "frag1" -> {
                v1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccentLight))
                v2.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                chooseIcon.setImageResource(R.drawable.ic_file_upload_black_24dp) // blue
                detailIcon.setImageResource(R.drawable.ic_file_upload_black_24dp) // light
                uploadIcon.setImageResource(R.drawable.ic_file_upload_black_24dp) // light
            }

            "frag2" -> {
                v1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccentLight))
                v2.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                chooseIcon.setImageResource(R.drawable.ic_file_upload_black_24dp) // blue
                detailIcon.setImageResource(R.drawable.ic_file_upload_black_24dp) // blue
                uploadIcon.setImageResource(R.drawable.ic_file_upload_black_24dp) // light
            }

            "frag3" -> {
                v1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccentLight))
                v2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccentLight))
                chooseIcon.setImageResource(R.drawable.ic_file_upload_black_24dp) // blue
                detailIcon.setImageResource(R.drawable.ic_file_upload_black_24dp) // blue
                uploadIcon.setImageResource(R.drawable.ic_file_upload_black_24dp) // blue
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) {
            onBackPressed()
        }

        return false
    }
}
