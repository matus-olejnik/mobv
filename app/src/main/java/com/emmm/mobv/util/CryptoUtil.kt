package com.emmm.mobv.util

import android.util.Base64
import android.util.Log
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class CryptoUtil {

    companion object {

        private fun processKey(myKey: String): SecretKeySpec? {
            var sha: MessageDigest? = null
            try {
                var byteArray = myKey.toByteArray(charset("UTF-8"))
                sha = MessageDigest.getInstance("SHA-1")
                byteArray = sha.digest(byteArray)
                byteArray = Arrays.copyOf(byteArray, 16)
                return SecretKeySpec(byteArray, "AES")
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            return null
        }

        fun encrypt(strToEncrypt: String, secret: String): String? {
            Log.i("CryptoUtil", "encrypting")
            try {
                val secretKeySpec = processKey(secret)
                val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
                return Base64.encodeToString(cipher.doFinal(strToEncrypt.toByteArray(charset("UTF-8"))), Base64.DEFAULT)
            } catch (e: Exception) {
                println("Error while encrypting: $e")
            }
            return null
        }

        fun decrypt(strToDecrypt: String?, secret: String): String? {
            Log.i("CryptoUtil", "decrypting")
            try {
                val secretKeySpec = processKey(secret)
                val cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
                return String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)))
            } catch (e: Exception) {
                println("Error while decrypting: $e")
            }
            return null
        }
    }
}