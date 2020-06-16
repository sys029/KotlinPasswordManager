package com.example.kotlinpasswordmanager

import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

class SimpleCrypto {
    private val iv = "fedcba9876543210" //Dummy iv (CHANGE IT!)
    private val ivspec: IvParameterSpec
    private val keyspec: SecretKeySpec
    private var cipher: Cipher? = null
    private val SecretKey = "0123456789abcdef" //Dummy secretKey (CHANGE IT!)

    @Throws(Exception::class)
    fun encrypt(text: String?): ByteArray? {
        if (text == null || text.length == 0) throw Exception("Empty string")
        var encrypted: ByteArray? = null
        encrypted = try {
            cipher!!.init(Cipher.ENCRYPT_MODE, keyspec, ivspec)
            cipher!!.doFinal(padString(text).toByteArray())
        } catch (e: Exception) {
            throw Exception("[encrypt] " + e.message)
        }
        return encrypted
    }

    @Throws(Exception::class)
    fun decrypt(code: String?): ByteArray? {
        if (code == null || code.length == 0) throw Exception("Empty string")
        var decrypted: ByteArray? = null
        decrypted = try {
            cipher!!.init(Cipher.DECRYPT_MODE, keyspec, ivspec)
            cipher!!.doFinal(hexToBytes(code))
        } catch (e: Exception) {
            throw Exception("[decrypt] " + e.message)
        }
        return decrypted
    }

    companion object {
        fun bytesToHex(data: ByteArray?): String? {
            if (data == null) {
                return null
            }
            val len = data.size
            var str = ""
            for (i in 0 until len) {
                str =
                    if (data[i] and 0xFF.toByte() < 16) str + "0" + Integer.toHexString((data[i] and 0xFF.toByte()).toInt()) else str + Integer.toHexString(
                        (data[i] and 0xFF.toByte()).toInt()
                    )
            }
            return str
        }

        fun hexToBytes(str: String?): ByteArray? {
            return if (str == null) {
                null
            } else if (str.length < 2) {
                null
            } else {
                val len = str.length / 2
                val buffer = ByteArray(len)
                for (i in 0 until len) {
                    buffer[i] = str.substring(i * 2, i * 2 + 2).toInt(16).toByte()
                }
                buffer
            }
        }

        private fun padString(source: String): String {
            var source = source
            val paddingChar = ' '
            val size = 16
            val x = source.length % size
            val padLength = size - x
            for (i in 0 until padLength) {
                source += paddingChar
            }
            return source
        }
    }

    init {
        ivspec = IvParameterSpec(iv.toByteArray())
        keyspec = SecretKeySpec(SecretKey.toByteArray(), "AES")
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding")
        } catch (e: NoSuchAlgorithmException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }
}