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
        println(string(getHash(pinCodeConfiguration, tbi(12902 + 45), tbi(45))))
//        assertEquals(
//            string(chooseOrdered(Pair(sourceNumbers, 4), tbi(30))),
//            "1840"
//        )
    }

    @Test
    fun testGetHash () {
        assertEquals(
            string(getHash(pinCodeConfiguration, tbi(13156), tbi(45))),
            "0643"
        )
    }

    @Test
    fun testGetPassword () {
        println(getPassword("anlong", "hahahahihi", "567-321", "78-43"))
    }
}