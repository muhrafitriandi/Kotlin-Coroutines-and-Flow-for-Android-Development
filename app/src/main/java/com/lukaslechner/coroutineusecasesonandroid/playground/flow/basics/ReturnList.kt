package com.lukaslechner.coroutineusecasesonandroid.playground.flow.basics

import com.lukaslechner.coroutineusecasesonandroid.playground.utils.printWithTimePassed
import java.math.BigInteger

fun main() {
    val startTime = System.currentTimeMillis()
    calculateFactorialOf2(5).forEach {
        printWithTimePassed(it, startTime)
    }
}

fun calculateFactorialOf2(n: Int): List<BigInteger> = buildList {
    var factorial = BigInteger.ONE

    for (i in 1..n) {
        Thread.sleep(10)
        factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
        add(factorial)
    }
}