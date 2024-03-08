package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines.coroutines_scopes

import kotlinx.coroutines.*

fun main() = runBlocking {

    val scope = CoroutineScope(Dispatchers.Default)

    scope.coroutineContext[Job]!!.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Parent job was cancelled")
        }
    }

    val childCoroutine1Job = scope.launch {
        delay(1000)
        println("Coroutine 1 completed")
    }
    childCoroutine1Job.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine 1 was cancelled!")
        }
    }

    scope.launch {
        delay(2000)
        println("Coroutine 2 completed")
    }.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutine 2 was cancelled!")
        }
    }

    childCoroutine1Job.cancelAndJoin()

    delay(3000)

    // scope.coroutineContext[Job]!!.cancelAndJoin()
}