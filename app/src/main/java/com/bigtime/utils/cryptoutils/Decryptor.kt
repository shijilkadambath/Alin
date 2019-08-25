package com.smb.smbapplication.utils.cryptoutils

import com.bigtime.utils.AppConstants
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException



class Decryptor @Throws(
    CertificateException::class,
    NoSuchAlgorithmException::class,
    KeyStoreException::class,
    IOException::class
)
constructor() {

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
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        InvalidAlgorithmParameterException::class
    )
    fun decryptData(encryptedData: ByteArray, encryptionIv: ByteArray): String {

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, encryptionIv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(AppConstants.KEYSTORE_ALIAS), spec)

        return String(cipher.doFinal(encryptedData), charset("UTF-8"))
    }

    @Throws(NoSuchAlgorithmException::class, UnrecoverableEntryException::class, KeyStoreException::class)
    private fun getSecretKey(alias: String): SecretKey {
        return (keyStore!!.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }

    companion object {

        private val TRANSFORMATION = "AES/GCM/NoPadding"
    }

}
