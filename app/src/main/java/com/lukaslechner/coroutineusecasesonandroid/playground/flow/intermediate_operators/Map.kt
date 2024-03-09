package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

suspend fun main() {
    println("========== FLOW ==========")
    flowOf(1, 2, 3, 4, 5)
        .map {
            println("$it x 10 = ${it * 10}")
            it * 10
        }
        .collect { collectedValue ->
            println(collectedValue)
        }

    println("========== LIST ==========")
    listOf(1, 2, 3, 4, 5)
        .map {
            println("$it x 10 = ${it * 10}")
            it * 10
        }
        .forEach { collectedValue ->
            println(collectedValue)
        }
}