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
        println(getPassword(
            "anlong",
            "zxcvqwer",
            "27",
            "234+4321*234^3+4",
            "34^2+345543*234*342",
            false
        ))
    }
}