package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines.coroutines_scopes

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val scope = CoroutineScope(Dispatchers.Default)

fun main() = runBlocking {
    val job = scope.launch {
        delay(100)
        println("Coroutines Completed")
    }

    job.invokeOnCompletion { throwable ->
        if (throwable is CancellationException) {
            println("Coroutines was cancelled!")
        }
    }

    delay(50)
    onDestroy()
}

fun onDestroy() {
    println("life-time of scope ends")
    scope.cancel()
}