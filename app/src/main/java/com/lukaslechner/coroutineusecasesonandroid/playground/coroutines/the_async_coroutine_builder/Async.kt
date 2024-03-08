package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines.the_async_coroutine_builder

import com.lukaslechner.coroutineusecasesonandroid.playground.utils.printWithTimePassed
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {

    val startTime = System.currentTimeMillis()

    val time = measureTimeMillis {
        val deferred1 = async {
            val result1 = networkCallAsync(1)
            printWithTimePassed("result 1 received: $result1", startTime)
            result1
        }

        val deferred2 = async {
            val result2 = networkCallAsync(2)
            printWithTimePassed("result 2 received: $result2", startTime)
            result2
        }

        val resultList = awaitAll(deferred1, deferred2)
        printWithTimePassed("result list: $resultList", startTime)
    }
    println("Total time: $time")
}

suspend fun networkCallAsync(number: Int): String {
    delay(500)
    return "Result $number"
}