package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines_and_threads

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val start = Date()
    val time = measureTimeMillis {
        repeat(1_000_000) {
            launch {
                delay(1000)
                print("${it + 1}, ")
            }
        }
    }

    println("Waiting...")
    delay(8_000)
    val end = Date()
    println("\nFinish from $start to $end")
    println("Total time: $time")
}