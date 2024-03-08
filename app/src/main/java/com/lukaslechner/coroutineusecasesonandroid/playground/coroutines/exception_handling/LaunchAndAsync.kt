package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines.exception_handling

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

fun main() {
//    handlingLaunch()
//    handlingAsync()
//    handlingAsyncAwait()
    handlingChildAsync()
}

fun handlingLaunch() {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler 1")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

    scope.launch {
        delay(200)
        throw RuntimeException()
    }

    Thread.sleep(1000)
}

fun handlingAsync() {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler 2")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

    scope.async {
        delay(200)
        throw RuntimeException()
    }

    Thread.sleep(1000)
}

fun handlingAsyncAwait() {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler 3")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

    val deferred = scope.async {
        delay(200)
        throw RuntimeException()
    }

    scope.launch {
        deferred.await()
    }

    Thread.sleep(1000)
}

fun handlingChildAsync() {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler 4")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

    scope.launch {
        supervisorScope {
            val a = async {
                delay(200)
                throw RuntimeException()
            }
            val b = async {
                delay(300)
                println("This is B")
            }

            listOf(a, b).mapNotNull {
                try {
                    it.await()
                } catch (e: Exception) {
                    println("Caught: $e")
                    null
                }
            }
        }
    }

    Thread.sleep(1000)
}