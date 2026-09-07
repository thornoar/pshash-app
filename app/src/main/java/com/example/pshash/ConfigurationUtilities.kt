package com.example.pshash

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Optional

class ConfigEntry(val pubkey: String, val config: Optional<List<Pair<List<Char>, Int>>>, val patch: Int)

fun parseConfig(src: String): List<ConfigEntry> {
    val res = mutableListOf<ConfigEntry>()
    for (str in src.lines()) {
        if (str.isEmpty()) continue
        if (str[0] == '#' || str[0] == ':') continue
        var start: Int = 0
        var offset: Int = 0
        val len = str.length

        var pubkeys: List<String> = listOf()
        var config: Optional<List<Pair<List<Char>, Int>>> = Optional.empty()
        var patch: Int = 0

        while (offset < len && str[offset] != ':') {
            offset += 1
        }
        if (offset == len) continue

        start = offset + 1
        offset = 0

        fun findArgument () {
            start += 1
            while (start < len && str[start].isWhitespace())
                start += 1
            while (start+offset < len && !str[start+offset].isWhitespace())
                offset += 1
        }

        while (start + 1 < len) {
            if (str[start] == '-') {
                start += 1
                if (str[start] == 'p') {
                    findArgument()
                    if (offset > 0) {
                        try {
                            patch = str.substring(start, start+offset).toInt()
                        } catch (_: NumberFormatException) {}
                    }
                }
                if (str[start] == 'k') {
                    findArgument()
                    if (offset > 0) {
                        val argconf = getConfiguration(str.substring(start, start+offset))
                        if (argconf.isNotEmpty()) {
                            config = Optional.of(argconf)
                        }
                    }
                }
                if (str[start] == 'n') {
                    findArgument()
                    if (offset > 0) {
                        if (str[start] == '(' && str[start+offset-1] == ')') {
                            start += 1
                            offset -= 1
                            val stramts = str.substring(start, start+offset).split(",")
                            if (stramts.size == 4) {
                                try {
                                    config = Optional.of(listOf(Pair(sourceLower, stramts[0].toInt()), Pair(sourceUpper, stramts[1].toInt()), Pair(sourceSpecial, stramts[2].toInt()), Pair(sourceNumbers, stramts[3].toInt())))
                                } catch (e: NumberFormatException) {}
                            }
                        }
                    }
                }
                if (str[start] == 'c') {
                    // Unsupported yet
                }
            }
        }

        pubkeys = str.substring(start, offset).split(",").map { it.trim() }
        res.addAll(pubkeys.map { ConfigEntry(it, config, patch) })
    }

    return res
}

fun readConfigFile(context: Context): String {
    val temp = StringBuilder()
    try {
        val fin: FileInputStream = context.openFileInput("pshash.conf")
        var a: Int
        while (fin.read().also { a = it } != -1) {
            temp.append(a.toChar())
        }
    } catch (_: FileNotFoundException) {}

    return temp.toString()
}

fun writeConfigFile(
    context: Context,
    data: String
) {
    val fout: FileOutputStream = context.openFileOutput("pshash.conf", 0)
    fout.write(data.toByteArray())
}