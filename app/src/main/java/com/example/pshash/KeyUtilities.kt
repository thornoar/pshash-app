package com.example.pshash

import androidx.compose.ui.util.fastJoinToString
import java.math.BigInteger

fun isValidPublicKey(
    key: String
) : Boolean {
    return !key.isEmpty()
}

val commonCombinations = listOf(
    "or","un","el","is","it","us","of","ag","um","yu","in","er","es",
    "ti","re","te","le","ra","li","ri","ne","se","de","co","ro","la",
    "di","ca","ta","ve","he","si","me","pe","ni","lo","ma","mi","to",
    "ce","na","ho","ge","hi","ha","po","pa","no","ci","pi","ke","mo",
    "ba","be","sa","fi","bo","su","so","bi","tu","vi","gi","ru","ku",
    "ga","ko","qu","lu","ki","do","fe","fo","bu","da","we","va","fu",
    "wa","fa","mu","pu","go","wo","gu","du","nu","hu","vo","yi","ze",
    "ye","ju","jo","xi","ka","xe","ja","zi","je"
)

fun isValidPrivateKey(
    key: String,
    mnemonic: Boolean
) : Boolean {
    if (key.isEmpty()) return false

    if (mnemonic) {
        val len = key.length
        if (len % 2 == 1) {
            return false
        }
        for (i in 0..<len step 2) {
            if (!commonCombinations.contains(key.slice(i .. (i+1)))) {
                return false
            }
        }
    } else {
        if (key.contains('+')) {
            return key.split("+").all { str -> isValidPrivateKey(str, false) }
        }
        if (key.contains('*')) {
            return key.split("*").all { str -> isValidPrivateKey(str, false) }
        }
        if (key.contains('^')) {
            return key.split("^").all { str -> isValidPrivateKey(str, false) }
        }
        for (c in key) {
            if (!c.isDigit()) return false
        }
    }

    return true
}

fun getPublicKey(
    keyStr: String,
    patch: Int
) : BigInteger {
    var res = bigZero
    for (c in keyStr) {
        res *= tbi(128)
        res += tbi((c.code + patch) % 128)
    }
    return res
}

fun getPrivateKey(
    keyStr: String
) : BigInteger {
    val parts = keyStr.split("^", limit = 2)
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
    patch: String,
    choice: String,
    shuffle: String
) : String {
    val realConfig = getConfiguration(config)
    val realChoice = getPrivateKey(choice) + getPublicKey(public, patch.toInt())
    val realShuffle = getPrivateKey(shuffle)
    val hash = getHash(realConfig, realChoice, realShuffle)
    return hash.fastJoinToString(separator = "") { c -> c.toString() }
}

fun separate(
    str: String
) : List<String> {
    return str.toList().map { c -> c.toString() }
}