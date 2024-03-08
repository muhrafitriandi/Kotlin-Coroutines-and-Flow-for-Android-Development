package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines.coroutines_scopes

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val scope = CoroutineScope(Dispatchers.Default)

    val parentCoroutineJob = scope.launch {
        launch {
            delay(1000)
            println("Child Coroutine 1 has completed!")
        }
        launch {
            delay(2000)
            println("Child Coroutine 2 has completed!")
        }
    }

    parentCoroutineJob.join()
    println("Parent Coroutine has completed!")
}