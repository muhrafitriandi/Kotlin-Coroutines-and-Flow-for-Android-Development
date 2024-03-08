package com.lukaslechner.coroutineusecasesonandroid.playground.flow.basics

import java.math.BigInteger

fun main() {
    val result = calculateFactorialOf1(5)
    println("Result: $result")
}

fun calculateFactorialOf1(n: Int): BigInteger {
    var factorial = BigInteger.ONE

    for (i in 1..n) {
        Thread.sleep(10)
        factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
    }
    return factorial
}