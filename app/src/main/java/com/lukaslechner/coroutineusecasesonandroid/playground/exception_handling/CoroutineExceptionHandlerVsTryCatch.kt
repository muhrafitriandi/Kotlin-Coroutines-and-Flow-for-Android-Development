package com.lukaslechner.coroutineusecasesonandroid.playground.exception_handling

import kotlinx.coroutines.*

fun main() {
    coroutineExceptionHandler()
    tryCatch()
}

fun coroutineExceptionHandler() {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Caught exception: $throwable")
    }

    val scope = CoroutineScope(Job())

    scope.launch(exceptionHandler) {
        launch {
            println("Starting coroutine 1")
            delay(100)
            throw RuntimeException()
        }
        launch {
            println("Starting coroutine 2")
            delay(3000)
            println("Coroutine 2 completed")
        }
    }

    Thread.sleep(5000)
}

fun tryCatch() {
    val scope = CoroutineScope(Job())

    scope.launch {
        launch {
            println("Starting coroutine 1")
            delay(100)
            try {
                throw RuntimeException()
            }catch (e: Exception) {
                println("Caught exception: $e")
            }
        }
        launch {
            println("Starting coroutine 2")
            delay(3000)
            println("Coroutine 2 completed")
        }
    }

    Thread.sleep(5000)
}