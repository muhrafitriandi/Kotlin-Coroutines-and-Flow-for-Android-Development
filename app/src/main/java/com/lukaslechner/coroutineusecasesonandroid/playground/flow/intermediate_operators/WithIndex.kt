package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.withIndex

suspend fun main() {
    println("========== FLOW ==========")
    flowOf(1, 2, 3, 4, 5, 1)
        .withIndex()
        .collect { collectedValue ->
            println(collectedValue)
        }

    println("========== LIST ==========")
    listOf(1, 2, 3, 4, 5, 1)
        .withIndex()
        .forEach { collectedValue ->
            println(collectedValue)
        }

}