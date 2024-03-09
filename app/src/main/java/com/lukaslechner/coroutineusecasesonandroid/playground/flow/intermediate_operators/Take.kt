package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.takeWhile

suspend fun main() {
    println("========== FLOW ==========")
    flowOf(1, 2, 3, 4, 5)
        .takeWhile {
            println("$it < 3 = ${it < 3}")
            it < 3
        }
        .collect { collectedValue ->
            println(collectedValue)
        }

    println("========== LIST ==========")
    listOf(1, 2, 3, 4, 5)
        .takeWhile {
            println("$it < 3 = ${it < 3}")
            it < 3
        }
        .forEach { collectedValue ->
            println(collectedValue)
        }
}