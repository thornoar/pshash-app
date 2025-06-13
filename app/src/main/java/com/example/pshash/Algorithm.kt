package com.example.pshash
import java.math.BigInteger

fun tbi (n : Int) : BigInteger {
    return BigInteger.valueOf(n.toLong())
}

val bigZero = tbi(0)
val bigOne = tbi(1)

fun factorial (n : BigInteger) : BigInteger {
    return if (n == bigZero) tbi(1) else n * factorial(n - bigOne)
}

fun relativeFactorial (n : BigInteger, m : BigInteger) : BigInteger {
    return if (m == bigZero) bigOne else n * relativeFactorial(n - bigOne, m - bigOne)
}

fun shift (c : Char): BigInteger {
    return tbi(c.code)
}

fun shift2 (lst : List<Char>) : BigInteger {
    return lst.fold(bigZero) { acc, c ->
        acc + shift(c)
    }
}

fun shift3 (lst : List<List<Char>>) : BigInteger {
    return lst.fold(bigZero) { acc, c ->
        acc + shift2(c)
    }
}

val combineHashing : (
        (List<Pair<List<Char>, Int>>, BigInteger) -> List<Char>,
        (List<Char>, BigInteger) -> List<Char>
) -> (List<Pair<List<Char>, Int>>, BigInteger, BigInteger) -> List<Char> = { f, g ->
    { config, k1, k2 ->
        g(f(config, k1), k2)
    }
}

fun mapHashing (
    f : (Pair<List<Char>, Int>, BigInteger) -> List<Char>,
    spr : (Pair<List<Char>, Int>) -> BigInteger
) : (List<Pair<List<Char>, Int>>, BigInteger) -> List<List<Char>> {
    return { lst, key ->
        if (lst.isEmpty()) emptyList() else {
            val a : Pair<List<Char>, Int> = lst.first()
            val s = spr(a)
            val keyMod : BigInteger = key % s
            val keyDiv : BigInteger = key / s
            val b : List<Char> = f(a, keyMod)
            val prev : List<List<Char>> = mapHashing(f, spr)(lst.drop(1), keyDiv + shift2(b))
            listOf(b) + prev
        }
    }
}

fun composeHashing (
    f : (List<Pair<List<Char>, Int>>, BigInteger) -> List<List<Char>>,
    spr : (List<Pair<List<Char>, Int>>) -> BigInteger,
    g : (List<List<Char>>, BigInteger) -> List<Char>
) : (List<Pair<List<Char>, Int>>, BigInteger) -> List<Char> {
    return { a, key ->
        val s = spr(a)
        val keyMod = key % s
        val keyDiv = key / s
        val b = f(a, keyMod)
        g(b, keyDiv + shift3(b))
    }
}

val sourceLower : List<Char> = "ckapzfitqdxnwehrolmbyvsujg".toList()
val sourceUpper : List<Char> = "RQLIANBKJYVWPTEMCZSFDOGUHX".toList()
val sourceSpecial : List<Char> = "=!*@?$%#&-+^".toList()
val sourceNumbers : List<Char> = "1952074386".toList()

val defaultConfiguration : List<Pair<List<Char>, Int>> =
    listOf(Pair(sourceLower, 8), Pair(sourceUpper, 8), Pair(sourceSpecial, 5), Pair(sourceNumbers, 4))
val mediumConfiguration : List<Pair<List<Char>, Int>> =
    listOf(Pair(sourceLower, 5), Pair(sourceUpper, 5), Pair(sourceSpecial, 5), Pair(sourceNumbers, 5))
val shortConfiguration : List<Pair<List<Char>, Int>> =
    listOf(Pair(sourceLower, 4), Pair(sourceUpper, 4), Pair(sourceSpecial, 4), Pair(sourceNumbers, 4))
val anlongConfiguration : List<Pair<List<Char>, Int>> =
    listOf(Pair(sourceLower, 7), Pair(sourceUpper, 7), Pair(sourceNumbers, 7))
val anshortConfiguration : List<Pair<List<Char>, Int>> =
    listOf(Pair(sourceLower, 4), Pair(sourceUpper, 4), Pair(sourceNumbers, 4))
val pinCodeConfiguration : List<Pair<List<Char>, Int>> =
    listOf(Pair(sourceNumbers, 4))
val mediumPinCodeConfiguration : List<Pair<List<Char>, Int>> =
    listOf(Pair(sourceNumbers, 6))
val longPinCodeConfiguration : List<Pair<List<Char>, Int>> =
    listOf(Pair(sourceNumbers, 8))

fun chooseOrdered (
    src : Pair<List<Char>, Int>,
    key : BigInteger
) : List<Char> {
    if (src.second == 0 || src.first.isEmpty()) return emptyList()
    val len : BigInteger = tbi(src.first.size)
    val keyMod : Int = (key % len).toInt()
    val keyDiv : BigInteger = key / len
    val curElt : Char = src.first[keyMod]
    val prev : List<Char> = chooseOrdered(Pair(src.first.filter { c -> c != curElt }, src.second-1), keyDiv + shift(curElt))
    return listOf(curElt) + prev
}

val shuffleList : (
    List<Char>,
    BigInteger
) -> List<Char> = { lst, key ->
    chooseOrdered(Pair(lst, lst.size), key)
}

val chooseOrderedSpread : (
    Pair<List<Char>, Int>
) -> BigInteger = { src ->
    relativeFactorial(tbi(src.first.size), tbi(src.second))
}

val chooseOrderedSpreadMapped : (
    List<Pair<List<Char>, Int>>
) -> BigInteger = { srcs ->
    srcs.map { src -> chooseOrderedSpread(src) }.fold(bigOne, { acc, cur -> acc * cur})
}

val mergeTwoListsSpread : (
    BigInteger,
    BigInteger
) -> BigInteger = { m1, m2 ->
    factorial(m1 + m2) / (factorial(m1) * factorial(m2))
}

fun mergeTwoLists (
    src : Pair<List<Char>, List<Char>>,
    key : BigInteger
) : List<Char> {
    if (src.first.isEmpty()) return src.second
    if (src.second.isEmpty()) return src.first
    val elt1 : Char = src.first.first()
    val elt2 : Char = src.second.first()
    val spr1 : BigInteger = mergeTwoListsSpread(tbi(src.first.size-1), tbi(src.second.size))
    val spr2 : BigInteger = mergeTwoListsSpread(tbi(src.first.size), tbi(src.second.size-1))
    val curKey : BigInteger = key % (spr1 + spr2)
    if (curKey < spr1) {
        val prev : List<Char> = mergeTwoLists(Pair(src.first.drop(1), src.second), curKey + shift(elt1))
        return listOf(elt1) + prev
    } else {
        val prev : List<Char> = mergeTwoLists(Pair(src.first, src.second.drop(1)), curKey - spr1 + shift(elt2))
        return listOf(elt2) + prev
    }
}

fun mergeListsSpread (
    srcs : List<List<Char>>
) : BigInteger {
    var sum = 0
    var prod : BigInteger = bigOne
    for (src in srcs) {
        sum += src.size
        prod *= factorial(tbi(src.size))
    }
    return factorial(tbi(sum)) / prod
}

fun mergeLists (
    srcs : List<List<Char>>,
    key : BigInteger
) : List<Char> {
    if (srcs.isEmpty()) return emptyList()
    if (srcs.size == 1) return srcs[0]
    if (srcs.size == 2) return mergeTwoLists(Pair(srcs[0], srcs[1]), key)
    val l = srcs[0]
    val newSrcs = srcs.drop(1)
    val spr = mergeListsSpread(newSrcs)
    val keyMod = key % spr
    val keyDiv = key / spr
    return mergeTwoLists(Pair(l, mergeLists(newSrcs, keyMod)), keyDiv + shift2(l))
}

val chooseAndMerge : (
    List<Pair<List<Char>, Int>>,
    BigInteger
) -> List<Char> = composeHashing(
    mapHashing(
        { x, y -> chooseOrdered(x, y) },
        chooseOrderedSpread
    ),
    chooseOrderedSpreadMapped
) { x, y -> mergeLists(x, y) }

val getHash : (
    srcs : List<Pair<List<Char>, Int>>,
    BigInteger,
    BigInteger
) -> List<Char> = combineHashing(chooseAndMerge, shuffleList)