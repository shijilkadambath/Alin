package com.bigtime.utils.cryptoutils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import com.bigtime.utils.AppConstants
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.*



class Encryptor @Throws(
    CertificateException::class,
    NoSuchAlgorithmException::class,
    KeyStoreException::class,
    IOException::class
)

constructor() {

    var encryption: ByteArray? = null
        private set
    var iv: ByteArray? = null
        private set
    private var keyStore: KeyStore? = null

    init {
        initKeyStore()
    }

    @Throws(KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class, IOException::class)
    private fun initKeyStore() {
        keyStore = KeyStore.getInstance(AppConstants.ANDROID_KEY_STORE)
        keyStore!!.load(null)
    }

    @Throws(
        UnrecoverableEntryException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        NoSuchProviderException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IOException::class,
        InvalidAlgorithmParameterException::class,
        SignatureException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun encryptText(textToEncrypt: String): Pair<ByteArray, ByteArray> {

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(AppConstants.KEYSTORE_ALIAS))

        return Pair(cipher.doFinal(textToEncrypt.toByteArray(charset("UTF-8"))), cipher.iv)
    }

    fun getSecretKey(alias: String): SecretKey? {
        try {
            return if (!keyStore!!.containsAlias(alias)) {
                createNewSecretKey(alias)
            } else {
                (keyStore!!.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
            }
        } catch (e: Exception) {
            Log.e(TAG, Log.getStackTraceString(e))
        }

        return null
    }

    @Throws(NoSuchAlgorithmException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    private fun createNewSecretKey(alias: String): SecretKey {

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AppConstants.ANDROID_KEY_STORE)

        keyGenerator.init(
            KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
        )

        return keyGenerator.generateKey()
    }

    companion object {

        private val TAG = "Encryptor"
        private val TRANSFORMATION = "AES/GCM/NoPadding"
    }

}
