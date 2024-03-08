package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines.multithread_coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() = runBlocking {
    println("Program started")
    val job1 = launch { coroutines(1, 1000) }
    val job2 = launch { coroutines(2, 500) }
    joinAll(job1, job2)
    println("Program finished")
}

suspend fun coroutines(number: Int, delay: Long) {
    println("Coroutines start $number || ${Thread.currentThread().name}")
    delay(delay)
    withContext(Dispatchers.Default) {
        println("Coroutines finished $number || ${Thread.currentThread().name}")
    }
}