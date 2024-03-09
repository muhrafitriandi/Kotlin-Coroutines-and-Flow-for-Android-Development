package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf

suspend fun main() {
    println("========== FLOW ==========")
    flowOf(20, 2, 30, 4, 50, 6)
        .filter {
            println("$it > 10 = ${it > 10}")
            it > 10
        }
        .collect { collectedValue ->
            println(collectedValue)
        }

    println("========== LIST ==========")
    listOf(20, 2, 30, 4, 50, 6)
        .filter {
            println("$it > 10 = ${it > 10}")
            it > 10
        }
        .forEach { collectedValue ->
            println(collectedValue)
        }

}