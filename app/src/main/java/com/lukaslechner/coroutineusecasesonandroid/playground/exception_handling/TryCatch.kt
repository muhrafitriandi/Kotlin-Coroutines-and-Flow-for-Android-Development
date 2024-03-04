package com.lukaslechner.coroutineusecasesonandroid.playground.exception_handling

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun main() {
    val scope = CoroutineScope(Job())

    scope.launch {
        try {
            launch {
                functionThatThrows() // will terminate program / won't propagate to upwards. Need handling with try-catch again.
            }
        } catch (e: Exception) {
            println("Caught: $e")
        }
    }

    Thread.sleep(1000)
}

fun functionThatThrows() {
    throw RuntimeException()
}