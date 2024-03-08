package com.lukaslechner.coroutineusecasesonandroid.playground.flow.builders

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val singleValue = flowOf(1).collect { emittedValue ->
        println("Single value: $emittedValue")
    }

    flow {
        emit(1)
    }.collect { emittedValue ->
        println("flow Single value{}: $emittedValue")
    }

    val multipleValue = flowOf(1, 2, 3).collect { emittedValue ->
        println("Multiple value: $emittedValue")
    }

    flow {
        emitAll(flowOf(1, 2, 3))
    }.collect { emittedValue ->
        println("flow Multiple value{}: $emittedValue")
    }

    listOf("A", "B", "C").asFlow().collect { emittedValue ->
        println("asFlow(): $emittedValue")
    }

    flow {
        delay(2000)
        emit("item emitted after 2000ms")

        emitAll(flowOf("X", "Y", "Z"))
    }.collect { emittedValue ->
        println("flow{}: $emittedValue")
    }
}