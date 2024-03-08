package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines.coroutines_and_threads

import java.util.Date
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

fun main() {
    val start = Date()
    val time = measureTimeMillis {
        repeat(1_000_000) {
            thread {
                Thread.sleep(1000)
                print("${it + 1}, ")
            }
        }
    }

    println("Waiting...")
    Thread.sleep(6000)
    val end = Date()
    println("\nFinish from $start to $end")
    println("Total time: $time")
}