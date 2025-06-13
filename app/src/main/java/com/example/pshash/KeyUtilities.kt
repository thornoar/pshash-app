package com.example.pshash

import androidx.compose.ui.util.fastJoinToString
import java.math.BigInteger

fun isValidPublicKey(
    key: String
) : Boolean {
    if (key.isEmpty()) return false
    for (c in key) {
        if (c == ' ') return false
        if (c.isUpperCase()) return false
    }
    return true
}

fun isValidPrivateKey(
    key: String
) : Boolean {
    if (key.isEmpty()) return false
    var dashCount = 0
    var count = 0
    for (c in key) {
        count += 1
        if (c == '-') {
            if (dashCount > 0 || count == 1 || count == key.length) return false
            dashCount += 1
            continue
        }
        if (!c.isDigit()) return false
    }
    return true
}

fun getPublicKey(
    keyStr: String
) : BigInteger {
    var res = bigZero
    for (c in keyStr) {
        res *= tbi(128)
        res += tbi(c.code)
    }
    return res
}

fun getPrivateKey(
    keyStr: String
) : BigInteger {
    val parts = keyStr.split("-", limit = 2)
    if (parts.size == 1) return parts[0].toBigInteger()
    if (parts.size != 2) return bigZero
    val base = tbi(parts[0].toInt())
    val exp = parts[1].toInt()
    return base.pow(exp)
}

val availableConfigKeywords = arrayOf(
    "default",
    "long",
    "medium",
    "short",
    "anlong",
    "anshort",
    "pin",
    "mediumpin",
    "longpin"
)

fun getConfiguration(
    keyword: String
) : List<Pair<List<Char>, Int>> {
    return when (keyword) {
        "default" -> defaultConfiguration
        "long" -> defaultConfiguration
        "medium" -> mediumConfiguration
        "short" -> shortConfiguration
        "anlong" -> anlongConfiguration
        "anshort" -> anshortConfiguration
        "pin" -> pinCodeConfiguration
        "mediumpin" -> mediumPinCodeConfiguration
        "longpin" -> longPinCodeConfiguration
        else -> defaultConfiguration
    }
}

fun displayConfiguration(
    keyword: String
) : String {
    return when (keyword) {
        "default" -> "default"
        "long" -> "8 U., 8 l., 5 sp., 4 dig."
        "medium" -> "5 U., 5 l., 5 sp., 5 dig."
        "short" -> "4 U., 4 l., 4 sp., 4 dig."
        "anlong" -> "7 U., 7 l., 7 dig."
        "anshort" -> "4 U., 4 l., 4 dig."
        "pin" -> "4-digit pin code"
        "mediumpin" -> "6-digit pin code"
        "longpin" -> "8-digit pin code"
        else -> keyword
    }
}

fun getPassword(
    config: String,
    public: String,
    choice: String,
    shuffle: String
) : String {
    val realConfig = getConfiguration(config)
    val realChoice = getPrivateKey(choice) + getPublicKey(public)
    val realShuffle = getPrivateKey(shuffle)
    val hash = getHash(realConfig, realChoice, realShuffle)
    return hash.fastJoinToString(separator = "") { c -> c.toString() }
}