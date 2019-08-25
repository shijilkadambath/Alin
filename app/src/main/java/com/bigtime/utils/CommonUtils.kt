/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

package com.bigtime.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.util.Patterns
import androidx.annotation.NonNull
import androidx.core.content.res.ResourcesCompat
import com.bigtime.R


import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern


object CommonUtils {

    val PASSWORD = Pattern.compile("((?=.*\\d)(?=.*[A-Z]).{6,20})")

    val timestamp: String
        get() = SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.US).format(Date())

    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPhone(email: String): Boolean {
        return Patterns.PHONE.matcher(email).matches()
    }
    /*@Throws(IOException::class)
    fun loadJSONFromAsset(context: Context, jsonFileName: String): String {
        val manager = context.assets
        val `is` = manager.open(jsonFileName)

        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()

        return String(buffer, "UTF-8")
    }*/

    fun getUnformattedPhoneNumber(phone: String): String {

        return phone.replace(" ", "")
                .replace("(", "")
                .replace(")", "")
                .replace("-", "")
    }

    fun FONT_METROPOLIS_REGULAR(@NonNull context: Context): Typeface? {
        return ResourcesCompat.getFont(context, R.font.normal)
    }

    fun FONT_METROPOLIS_MEDIUM(@NonNull context: Context): Typeface? {
        return ResourcesCompat.getFont(context, R.font.medium)
    }

    fun FONT_METROPOLIS_LIGHT(@NonNull context: Context): Typeface? {
        return ResourcesCompat.getFont(context, R.font.light)
    }

}// This utility class is not publicly instantiable
