package com.bigtime.utils

/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

import com.bigtime.BuildConfig


object AppConstants {


    private const val PACKAGE_NAME = BuildConfig.APPLICATION_ID


    val DATABASE = "app.db"

    const val PREFERENCE_NAME = PACKAGE_NAME + "_pref"

    val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss"


    val TEMP_PHOTO_FILE = "image.jpeg"
    val TEMP_VIDEO_FILE = "video.mp4"


    const val APP_KEY = "Leaf"


    var HOST_LOGIN = "devpyapi4.shoekonnect.com"

     var SCHEME = "http"
     var PORT = 80
     var HOST = "dev4.shoekonnect.com"




    const val APP_DEVICE = "Android"

    const val KEYSTORE_ALIAS = "LEAF_ALIAS"
    const val ANDROID_KEY_STORE = "AndroidKeyStore"


    const val PRE_SESSION = "$PACKAGE_NAME.login_session"
    const val PRE_AUTH_TOKEN = "$PACKAGE_NAME.auth"
    const val PRE_REFRESH_TOKEN = "$PACKAGE_NAME.refresh"
    const val PRE_PASSWORD = "$PACKAGE_NAME.password"
    const val PRE_PASSWORD_IV = "$PACKAGE_NAME.password_iv"
    const val PRE_FCM = "$PACKAGE_NAME.fcm"
    const val PRE_IS_LOGGED_IN = "$PACKAGE_NAME.is_logged_in"
    const val PRE_IS_NEW_REGISTRATION = "$PACKAGE_NAME.is_new_registration"
}
