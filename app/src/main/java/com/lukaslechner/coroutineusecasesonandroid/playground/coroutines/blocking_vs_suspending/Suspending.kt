package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines.blocking_vs_suspending

import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("Program started")
    val job1 = launch { coroutines(1, 1000) }
    val job2 = launch { coroutines(2, 500) }
    val job3 = launch {
        repeat(5) {
            println("${it + 1} || ${Thread.currentThread().name}")
            delay(250)
        }
    }
    joinAll(job1, job2, job3)
    println("Program finished")
}

suspend fun coroutines(number: Int, delay: Long) {
    println("Coroutines start $number || ${Thread.currentThread().name}")
    delay(delay)
    println("Coroutines finished $number || ${Thread.currentThread().name}")
}