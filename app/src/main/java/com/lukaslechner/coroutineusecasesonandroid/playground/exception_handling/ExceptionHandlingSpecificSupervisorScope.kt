package com.lukaslechner.coroutineusecasesonandroid.playground.exception_handling

import kotlinx.coroutines.*

fun main() {

    val ceh = CoroutineExceptionHandler { _, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler")
    }

    val scope = CoroutineScope(Job())

    scope.launch(ceh) {
        try {
            supervisorScope {
                launch { // using same scope
                    println("CEH: ${coroutineContext[CoroutineExceptionHandler]}")
                    throw RuntimeException()
                }
            }
        } catch (e: Exception) {
            println("Caught $e")
        }
    }

    Thread.sleep(100)
}