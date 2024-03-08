package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines.the_async_coroutine_builder

import com.lukaslechner.coroutineusecasesonandroid.playground.utils.printWithTimePassed
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {

    val startTime = System.currentTimeMillis()
    val resultList = mutableListOf<String>()

    val time = measureTimeMillis {
        networkCallLaunch(3) // will be blocked

        val job1 = launch {
            val result1 = networkCallLaunch(1)
            resultList.add(result1)
            printWithTimePassed("result 1 received: $result1", startTime)
        }

        val job2 = launch {
            val result2 = networkCallLaunch(2)
            resultList.add(result2)
            printWithTimePassed("result 2 received: $result2", startTime)
        }
        joinAll(job1, job2)
        printWithTimePassed("result list: $resultList", startTime)
    }
    println("Total time: $time")
}

suspend fun networkCallLaunch(number: Int): String {
    delay(500)
    return "Result $number"
}