package sebogo.lin.noteappbyced.utilities

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun String.dateInFormat(format: String): Date? {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    var parsedDate: Date? = null
    try {
        dateFormat.isLenient = false
        parsedDate = dateFormat.parse(this)
    } catch (ignored: ParseException) {
        return null
    }
    return parsedDate
}

fun String.digits() = this.replace("[^\\d]".toRegex(), "")

fun String?.isStringValid(): Boolean = this != null && this.isNotBlank() && this.isNotEmpty() &&   (this.trim().isNotEmpty())

fun String.isDate(): Boolean = this.digits().length == 8

fun String.isPhone(): Boolean {
    val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)
}
fun String.md5() = encrypt(this, "MD5")

fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}


fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}


private fun encrypt(string: String?, type: String): String {
    if (string.isNullOrEmpty()) {
        return ""
    }
    val md5: MessageDigest
    return try {
        md5 = MessageDigest.getInstance(type)
        val bytes = md5.digest(string!!.toByteArray())
        bytes2Hex(bytes)
    } catch (e: NoSuchAlgorithmException) {
        ""
    }
}

private fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}