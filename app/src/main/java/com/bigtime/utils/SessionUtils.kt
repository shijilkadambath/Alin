package com.bigtime.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.smb.smbapplication.utils.cryptoutils.Decryptor
import com.smb.smbapplication.utils.cryptoutils.Encryptor
import java.security.KeyStore
import javax.inject.Singleton



@Singleton
class SessionUtils {

    inner class LoginSession {

        @SerializedName("userid")
        @Expose
        var userId: Int? = null
        @SerializedName("emailverified")
        @Expose
        var emailVerified: Int? = null
        @SerializedName("username")
        @Expose
        var username: String? = null
        @SerializedName("mobileverified")
        @Expose
        var mobileverified: Int? = null
        @SerializedName("companyname")
        @Expose
        var companyName: String? = null
        @SerializedName("companylogo")
        @Expose
        var companyLogo: String? = null
        @SerializedName("companyphone")
        @Expose
        var companyPhone: String? = null
        @SerializedName("concernperson")
        @Expose
        var concernPerson: String? = null
        @SerializedName("tinno")
        @Expose
        var tinNo: String? = null
        @SerializedName("isbrandedseller")
        @Expose
        var brandedSeller: Int? = null
        @SerializedName("isselleractive")
        @Expose
        var sellerActive: Int? = null
        @SerializedName("isactive")
        @Expose
        var active: Int? = null
        @SerializedName("isseller")
        @Expose
        var seller: Int? = null
    }

    companion object {

        const val TAG = "SessionUtils"

        lateinit var preferences: SharedPreferences

        fun init(context: Context) {
            preferences = context.getSharedPreferences(AppConstants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        val loginSession: LoginSession?
            get() {
                return try {
                    val gson = Gson()
                    val json = preferences.getString(AppConstants.PRE_SESSION, "")
                    gson.fromJson(json, LoginSession::class.java)
                } catch (e: Exception) {
                    null
                }
            }

        fun saveSession(session: LoginSession?) {
            if (session == null) return
            val prefsEditor = preferences.edit()
            val gson = Gson()
            val json = gson.toJson(session)
            prefsEditor.putString(AppConstants.PRE_SESSION, json)
            prefsEditor.apply()
        }

        fun hasSession(): Boolean {
            return loginSession != null
        }

        fun clearSession() {
            preferences.edit().clear().apply()
            try {
                val keyStore = KeyStore.getInstance(AppConstants.ANDROID_KEY_STORE)
                keyStore!!.load(null)
                keyStore.deleteEntry(AppConstants.KEYSTORE_ALIAS)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        val isLoggedIn: Boolean?
            get() {
                return preferences.getBoolean(AppConstants.PRE_IS_LOGGED_IN, false)
            }

        fun setIsLoggedIn(loggedIn: Boolean) {
            preferences.edit().putBoolean(AppConstants.PRE_IS_LOGGED_IN, loggedIn).apply()
        }

        val isNewRegistration: Boolean
            get() {
                return preferences.getBoolean(AppConstants.PRE_IS_NEW_REGISTRATION, false)
            }

        fun setIsNewRegistration(isNewRegistration: Boolean) {
            preferences.edit().putBoolean(AppConstants.PRE_IS_NEW_REGISTRATION, isNewRegistration).apply()
        }

        val getFCMToken: String?
            get() {
                return preferences.getString(AppConstants.PRE_FCM, "")
            }

        fun saveFCMToken(fcmToken: String) {
            preferences.edit().putString(AppConstants.PRE_FCM, fcmToken).apply()
        }


        fun getAuthTokens(isAuthToken: Boolean = true): String? {
            return if (isAuthToken)
                preferences.getString(AppConstants.PRE_AUTH_TOKEN, "")
            else
                preferences.getString(AppConstants.PRE_REFRESH_TOKEN, "")
        }

        fun saveAuthTokens(auth: String, refresh: String) {
            preferences.edit().putString(AppConstants.PRE_AUTH_TOKEN, auth)
                .putString(AppConstants.PRE_REFRESH_TOKEN, refresh).apply()

        }

        fun getUserPassword(): String {
            try {
                return Decryptor().decryptData(
                    Base64.decode(preferences.getString(AppConstants.PRE_PASSWORD, ""), Base64.DEFAULT),
                    Base64.decode(preferences.getString(AppConstants.PRE_PASSWORD_IV, ""), Base64.DEFAULT)
                )
            } catch (e: Exception) {
                Log.e(TAG, "Decryption failed with: " + e.message, e)
            }
            return ""
        }

        fun saveUserPassword(password: String) {

            try {
                val keyStore = KeyStore.getInstance(AppConstants.ANDROID_KEY_STORE)
                keyStore!!.load(null)
                keyStore.deleteEntry(AppConstants.KEYSTORE_ALIAS)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            try {
                val encryptedTextDetails = Encryptor().encryptText(password)
                preferences.edit().putString(
                    AppConstants.PRE_PASSWORD,
                    Base64.encodeToString(encryptedTextDetails.first, Base64.DEFAULT)
                )
                    .putString(
                        AppConstants.PRE_PASSWORD_IV,
                        Base64.encodeToString(encryptedTextDetails.second, Base64.DEFAULT)
                    )
                    .apply()
            } catch (e: Exception) {
                Log.e(TAG, "Encryption failed with: " + e.message, e)
            }
        }
    }
}
