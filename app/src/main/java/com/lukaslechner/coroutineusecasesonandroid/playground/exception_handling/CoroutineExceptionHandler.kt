package com.lukaslechner.coroutineusecasesonandroid.playground.exception_handling

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun main() {

    val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Caught $exception in CoroutineExceptionHandler")
    }

    val scope = CoroutineScope(Job())

    scope.launch {
        launch(exceptionHandler) { // will not use and get exception, exception handler must be place at top level coroutine builder.
            functionThatThrowsIt()
        }
    }

    Thread.sleep(1000)
}

fun functionThatThrowsIt() {
    throw RuntimeException()
}