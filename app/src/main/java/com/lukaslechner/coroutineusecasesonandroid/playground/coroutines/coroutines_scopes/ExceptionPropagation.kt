package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines.coroutines_scopes

import kotlinx.coroutines.*

fun main() {

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Caught exception $exception")
    }

    val scope = CoroutineScope(SupervisorJob() + exceptionHandler)

    scope.launch {
        println("Coroutine 1 starts")
        delay(1000)
        println("Coroutine 1 fails")
        throw RuntimeException()
    }

    scope.launch {
        println("Coroutine 2 starts")
        delay(2000)
        println("Coroutine 2 completed")
    }.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine 2 got cancelled!")
        }
    }

    Thread.sleep(2500)

    println("Scope got cancelled: ${!scope.isActive}")

}