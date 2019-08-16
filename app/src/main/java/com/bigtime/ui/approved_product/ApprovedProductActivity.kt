package com.bigtime.ui.approved_product


import android.os.Bundle
import com.bigtime.R
import com.bigtime.ui.BaseActivity
import android.content.Intent
import android.view.MenuItem


class ApprovedProductActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approved_prodcut)

        //supportActionBar!!.setDisplayHomeAsUpEnabled(true);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home) {
            onBackPressed()
        }

        return false
    }
}
