package com.example.pshash

import androidx.compose.ui.util.fastJoinToString
import org.junit.Test

import org.junit.Assert.*

fun string (
    l : List<Char>
) : String {
    return l.fastJoinToString(separator = "") { c -> c.toString() }
}

class ExampleUnitTest {
    @Test
    fun test() {
        println(string(getHash(pinCodeConfiguration, tbi(12902 + 45), tbi(45))))
    }
}