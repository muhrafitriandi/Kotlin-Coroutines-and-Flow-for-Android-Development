package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exception_handling

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

suspend fun main(): Unit = coroutineScope {

    val stocksFlow = stocksFlow()

    // problem(stocksFlow)
    // uglySolution(stocksFlow)
    bestSolution(stocksFlow)
}

private suspend fun problem(stocksFlow: Flow<String>) {
    stocksFlow
        .onCompletion { cause ->
            if (cause == null) {
                println("Flow completed successfully!")
            } else {
                println("Flow completed exceptionally with $cause")
            }
        }.catch { throwable ->
            println("Handle exception in catch() operator $throwable")
        }.collect { stock ->
            throw Exception("Exception in collect{}")
            println("Collected $stock")
        }
}

private suspend fun uglySolution(stocksFlow: Flow<String>) {
    stocksFlow
        .onCompletion { cause ->
            if (cause == null) {
                println("Flow completed successfully!")
            } else {
                println("Flow completed exceptionally with $cause")
            }
        }.catch { throwable ->
            println("Handle exception in catch()-1 operator $throwable")
        }.collect { stock ->
            try {
                throw Exception("Exception in collect{}")
                println("Collected $stock")
            } catch (e: Exception) {
                println("Handle exception in catch()-2 operator $e")
            }
        }
}

private fun CoroutineScope.bestSolution(stocksFlow: Flow<String>) {
    stocksFlow
        .onCompletion { cause ->
            if (cause == null) {
                println("Flow completed successfully!")
            } else {
                println("Flow completed exceptionally with $cause")
            }
        }
        .onEach { stock ->
            throw Exception("Exception in collect{}")
            println("Collected $stock")
        }.catch { throwable ->
            println("Handle exception in catch() operator $throwable")
        }
        .launchIn(this)
}

private fun stocksFlow(): Flow<String> = flow {
    emit("Apple")
    emit("Microsoft")

    throw Exception("Network Request Failed!")
}