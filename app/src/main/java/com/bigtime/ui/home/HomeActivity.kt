package com.bigtime.ui.home


import android.os.Bundle
import android.view.WindowManager
import com.bigtime.R
import com.bigtime.ui.BaseActivity
import com.bigtime.ui.login.LoginActivity
import com.bigtime.utils.SessionUtils
import org.jetbrains.anko.intentFor

/**
 * Created by Ȿ₳Ɲ @ NEWAGESMB on Thursday, May 23, 2019
 */

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        if (!SessionUtils.hasSession() || !SessionUtils.isLoggedIn!!) {
            startActivity(intentFor<LoginActivity>())
            super.onCreate(savedInstanceState)
            finishAffinity()
            return
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }

}
