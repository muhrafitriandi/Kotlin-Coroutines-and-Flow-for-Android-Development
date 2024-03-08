package com.lukaslechner.coroutineusecasesonandroid.playground.flow.basics

import com.lukaslechner.coroutineusecasesonandroid.playground.utils.printWithTimePassed
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.math.BigInteger

fun main(): Unit = runBlocking {
    val startTime = System.currentTimeMillis()

    launch {
        calculateFactorialOf4(5).collect {
            printWithTimePassed(it, startTime)
        }
    }
    println("Ready for more work!")
}

fun calculateFactorialOf4(n: Int): Flow<BigInteger> = flow {
    var factorial = BigInteger.ONE

    for (i in 1..n) {
        delay(10)
        factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
        emit(factorial)
    }
}