package com.bigtime

/**
 * Created by Shijil Kadambath on 03/08/2018
 * Email : shijilkadambath@gmail.com
 */

import android.app.Activity
import android.app.Application
import com.bigtime.di.AppInjector
import com.bigtime.utils.SessionUtils
import com.bigtime.utils.logger.Log
import com.bigtime.utils.logger.LogWrapper
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class SheokonectionApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>



    override fun onCreate() {
        super.onCreate()


        AppInjector.init(this)
        Log.setLogNode(LogWrapper()) // Initialise logging
        SessionUtils.init(this)
        val config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build()
        Fresco.initialize(this, config)



       /* if (!LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            // return;
            LeakCanary.install(this)
        }*/

    }

    override fun activityInjector() = dispatchingAndroidInjector
}
