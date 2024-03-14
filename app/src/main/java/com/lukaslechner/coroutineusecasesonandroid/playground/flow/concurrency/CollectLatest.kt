package com.lukaslechner.coroutineusecasesonandroid.playground.flow.concurrency

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

suspend fun main() = coroutineScope {

    val flow = flow {
        repeat(5) {
            val pancakeIndex = it + 1
            println("Emitter:    Start Cooking Pancake $pancakeIndex")
            delay(2_000)
            println("Emitter:    Pancake $pancakeIndex ready!")
            emit(pancakeIndex)
        }
    }

    flow.collectLatest {
        println("Collector:  Start eating pancake $it")
        delay(10_000)
        println("Collector:  Finished eating pancake $it")
    }
}