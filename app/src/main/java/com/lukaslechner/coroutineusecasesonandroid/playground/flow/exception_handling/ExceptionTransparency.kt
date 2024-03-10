package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exception_handling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

suspend fun main(): Unit = coroutineScope {

    exceptionTransparency1()
    println("----------")
    exceptionTransparency2()
}

private suspend fun exceptionTransparency1() {
    flow {
        try {
            emit(1)
        } catch (e: Exception) {
            println("Catch exception in flow builder.")
        }
    }.collect { emittedValue ->
        throw Exception("Exception in collect{}")
    }
}

private suspend fun exceptionTransparency2() {
    flow {
        emit(1)
    }.catch {
        println("Catch exception in flow builder.")
    }.collect { emittedValue ->
        throw Exception("Exception in collect{}")
    }
}