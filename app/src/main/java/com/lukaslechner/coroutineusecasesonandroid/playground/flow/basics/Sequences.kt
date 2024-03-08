package com.lukaslechner.coroutineusecasesonandroid.playground.flow.basics

import com.lukaslechner.coroutineusecasesonandroid.playground.utils.printWithTimePassed
import java.math.BigInteger

fun main() {
    val startTime = System.currentTimeMillis()
    calculateFactorialOf3(5).forEach {
        printWithTimePassed(it, startTime)
    }
    println("Ready for more work!")
}

fun calculateFactorialOf3(n: Int): Sequence<BigInteger> = sequence {
    var factorial = BigInteger.ONE

    for (i in 1..n) {
        Thread.sleep(10)
        factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
        yield(factorial)
    }
}