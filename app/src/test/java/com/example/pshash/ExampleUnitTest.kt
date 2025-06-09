package com.example.pshash

import androidx.compose.ui.util.fastJoinToString
import org.junit.Test

import org.junit.Assert.*
//import com.example.pshash.*
import java.util.LinkedList

fun string (
    l : List<Char>
) : String {
    return l.fastJoinToString(separator = "") { c -> c.toString() }
}

class ExampleUnitTest {
    @Test
    fun test() {
        assertEquals(
            string(chooseOrdered(Pair(sourceNumbers, 4), tbi(30))),
            "1840"
        )
    }

    @Test
    fun testGetHash () {
        assertEquals(
            string(getHash(defaultConfiguration, tbi(123), tbi(234))),
            "KtRSi9HlW6#w%T\$Zxy=*b3a0E"
        )
    }
}