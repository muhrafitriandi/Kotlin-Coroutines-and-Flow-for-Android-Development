package com.lukaslechner.coroutineusecasesonandroid.playground.flow.concurrency

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.transformLatest

suspend fun main() = coroutineScope {

    val flow = flow {
        repeat(5) {
            val pancakeIndex = it + 1
            println("Emitter:    Start Cooking Pancake $pancakeIndex")
            delay(2000)
            println("Emitter:    Pancake $pancakeIndex ready!")
            emit(pancakeIndex)
        }
    }.transformLatest {
        println("Add topping onto the pancake $it")
        delay(4000)
        println("---after 4sec---")
        emit(it)
        emit(it * 10)
    }

    flow.collect {
        println("Collector:  Start eating pancake $it")
        delay(10_000)
        println("---after 10sec---")
        println("Collector:  Finished eating pancake $it")
    }
}