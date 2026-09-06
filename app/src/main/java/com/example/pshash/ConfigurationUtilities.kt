package com.example.pshash

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.Optional

class Config(config: Optional<List<Pair<List<Char>, Int>>>, patch: Int)

fun parseConfig(src: String): MutableMap<String, Config> {
    val res = mutableMapOf<String, Config>()
    for (str in src.lines()) {
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
        val pubkeysStr = str.substring(start, offset)

        start = offset + 1
        offset = 0

        while (start < len) {
            
        }

        pubkeys = pubkeysStr.split(",").map { it.trim() }
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
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return temp.toString()
}

fun writeConfigFile(
    context: Context,
    data: String
) {
    val fout: FileOutputStream = context.openFileOutput("pshash.conf", 0)
    fout.write(data.toByteArray())
}