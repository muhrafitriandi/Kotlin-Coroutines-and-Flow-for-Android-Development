package com.lukaslechner.coroutineusecasesonandroid.playground.coroutine_builders

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val scope = CoroutineScope(Dispatchers.Default)

    launchFunction()
    println("----------")
    runBlockingFunction()
    println("----------")
    println("start")
    val job = scope.launch {
        val result = fetchDataFromNetwork()
        println("Result: $result")
    }
    runBlocking {
        job.join()
        println("end")
    }
}

suspend fun fetchDataFromNetwork(): String = coroutineScope {
    delay(500)
    "Some task"
}

fun launchFunction() {
    val scope = CoroutineScope(Dispatchers.Default)
    println("start")
    val job = scope.launch {
        delay(500)
        println("Some task")
    }

    runBlocking {
        job.join()
        println("end")
    }
}

fun runBlockingFunction() = runBlocking {
    println("start")
    delay(500)
    println("Some task")
    println("end")
}