package com.lukaslechner.coroutineusecasesonandroid.playground.cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {

    val job = launch(Dispatchers.Default) {
        repeat(10) { index ->
            // option 1:
            // ensureActive()

            // option 2:
            // yield()

            // option 3:
            if (isActive) {
                println("operation number $index")
                Thread.sleep(100)
            } else {
                println("Clean up done!")
                throw CancellationException()
            }
        }
    }

    delay(250)
    println("Cancelling Coroutine")
    job.cancel()
}