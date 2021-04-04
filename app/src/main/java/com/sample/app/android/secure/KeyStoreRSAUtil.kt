package com.sample.app.android.secure

import android.content.ContentValues.TAG
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPublicKey
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.NoSuchPaddingException


object KeyStoreRSAUtil {

    private var keyStore: KeyStore? = null
    private const val KEY_STORE_TYPE = "AndroidKeyStore"

    fun createNewKeys(alias: String) {
        try {
            loadKeyStore()
            // Create new key if needed
            if (!keyStore!!.containsAlias(alias)) {
                val kpg: KeyPairGenerator =
                    KeyPairGenerator.getInstance(
                        KeyProperties.KEY_ALGORITHM_RSA,
                        KEY_STORE_TYPE
                    )

                kpg.initialize(
                    KeyGenParameterSpec.Builder(
                        alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setDigests(KeyProperties.DIGEST_SHA256)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .build()
                )

                kpg.generateKeyPair()
            } else {
                if (deleteKey(alias)) {
                    createNewKeys(alias)
                }
            }
        } catch (e: Exception) {
            Log.i(TAG, "Unable to create key $e")
        }
    }


    /*
      Encryption need public key:
    */
    fun encryptString(alias: String?, txtToEncrypt: String): String {
        var encryptedText = ""
        try {
            loadKeyStore()
            val privateKeyEntry: KeyStore.PrivateKeyEntry =
                keyStore?.getEntry(alias, null) as KeyStore.PrivateKeyEntry
            val publicKey: RSAPublicKey =
                privateKeyEntry.certificate.publicKey as RSAPublicKey
            if (txtToEncrypt.isEmpty()) {
                return ""
            }
            val inCipher: Cipher = getCipherInstance()
            inCipher.init(Cipher.ENCRYPT_MODE, publicKey)
            val outputStream = ByteArrayOutputStream()
            val chipTextOutStream = CipherOutputStream(
                outputStream, inCipher
            )
            chipTextOutStream.write(txtToEncrypt.toByteArray(StandardCharsets.UTF_8))
            chipTextOutStream.close()

            //Encrypted text
            encryptedText = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        } catch (e: Exception) {
            Log.i(TAG, Log.getStackTraceString(e))
        }
        return encryptedText
    }

    @Throws(NoSuchAlgorithmException::class, NoSuchPaddingException::class)
    private fun getCipherInstance(): Cipher {
        return Cipher.getInstance("RSA/ECB/PKCS1Padding")
    }

    fun decryptString(alias: String?, encryptedTxt: String?): String {
        var decryptedTxt = ""
        try {
            loadKeyStore()
            val privateKeyEntry: KeyStore.PrivateKeyEntry =
                keyStore?.getEntry(alias, null) as KeyStore.PrivateKeyEntry

            val output: Cipher = getCipherInstance()
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)

            val decode = Base64.decode(
                encryptedTxt,
                Base64.DEFAULT
            )
            val cipherInputStream = CipherInputStream(ByteArrayInputStream(decode), output)

            val values: ArrayList<Byte> = ArrayList()
            var nextByte: Int
            while (cipherInputStream.read().also { nextByte = it } != -1) {
                values.add(nextByte.toByte())
            }

            // decrypted text
            decryptedTxt = String(values.toByteArray(), 0, values.toByteArray().size, StandardCharsets.UTF_8)
            cipherInputStream.close()
        } catch (e: Exception) {
            Log.i(TAG, Log.getStackTraceString(e))
        }
        return decryptedTxt
    }


    private fun deleteKey(alias: String?): Boolean {
        var returnStatus: Boolean
        try {
            loadKeyStore()
            returnStatus = if (keyStore!!.containsAlias(alias)) {
                keyStore!!.deleteEntry(alias)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            returnStatus = false
            Log.i(TAG, "Error while deleting Key store $e")
        }
        return returnStatus
    }

    private fun loadKeyStore() {
        if (keyStore == null) {
            keyStore = KeyStore.getInstance(KEY_STORE_TYPE).apply { load(null) }
        }
    }

}