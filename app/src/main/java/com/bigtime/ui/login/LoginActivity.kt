package com.bigtime.ui.login



/**
 * Created by Shijil Kadambath on 03/08/2018
 *Email : shijilkadambath@gmail.com
 */


import android.os.Bundle
import com.bigtime.R
import com.bigtime.ui.BaseActivity
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class LoginActivity : BaseActivity()  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


}
