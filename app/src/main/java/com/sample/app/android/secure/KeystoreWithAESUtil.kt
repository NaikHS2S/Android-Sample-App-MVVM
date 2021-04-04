package com.sample.app.android.secure

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.UnrecoverableEntryException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


object KeystoreWithAESUtil {

    private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val SEPARATOR = ","

    private var keyStore: KeyStore? = null
    private var secretKey: SecretKey? = null

    fun initialize(keyName: String?) {
        initKeystore()
        if(keyName != null) {
            loadOrGenerateKey(keyName)
        }
    }

    private fun loadOrGenerateKey(keyName: String) {
        getKey(keyName)
        if (secretKey == null) generateKey(keyName)
    }

    private fun initKeystore() {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
    }

    private fun getKey(keyName: String) {
        try {
            val secretKeyEntry: KeyStore.SecretKeyEntry =
                keyStore?.getEntry(keyName, null) as KeyStore.SecretKeyEntry
            // if no key was found -> generate new
            secretKey = secretKeyEntry.secretKey
        } catch (e: KeyStoreException) {
            // failed to retrieve -> will generate new
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnrecoverableEntryException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
            keyStore?.deleteEntry(keyName)
            generateKey(keyName)
        }
    }

    private fun generateKey(keyName: String) {
        val keyGenerator: KeyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .build()
        keyGenerator.init(keyGenParameterSpec)
        secretKey = keyGenerator.generateKey()
    }


    fun encrypt(toEncrypt: String): String? {
        val cipher: Cipher = Cipher.getInstance(getTransformation())
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv: String = Base64.encodeToString(cipher.iv, Base64.NO_WRAP)
        val encrypted: String = Base64.encodeToString(
            cipher.doFinal(toEncrypt.toByteArray(StandardCharsets.UTF_8)),
            Base64.DEFAULT
        )
        return encrypted + SEPARATOR + iv
    }

    fun decrypt(toDecrypt: String): String? {
        val parts = toDecrypt.split(SEPARATOR).toTypedArray()
        if (parts.size == 2) {
            return try {
                val encrypted: ByteArray = Base64.decode(parts[0], Base64.NO_WRAP)
                val iv: ByteArray = Base64.decode(parts[1], Base64.NO_WRAP)
                val cipher: Cipher = Cipher.getInstance(getTransformation())
                val spec = IvParameterSpec(iv)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
                String(cipher.doFinal(encrypted), StandardCharsets.UTF_8)
            } catch (e: Exception) {
                ""
            }
        }
        return ""
    }

    private fun getTransformation() = KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7
}