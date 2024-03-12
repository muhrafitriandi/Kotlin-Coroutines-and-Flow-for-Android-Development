package com.lukaslechner.coroutineusecasesonandroid.playground.flow.hot_and_cold_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

fun main() {
    val sharedFlow = MutableSharedFlow<Int>()
    val scope = CoroutineScope(Dispatchers.Default)

    scope.launch {
        repeat(5) {
            delay(200)
            println("SharedFlow emit: $it")
            sharedFlow.emit(it)
        }
    }

    scope.launch {
        sharedFlow.collect {
            println("Collected A - $it")
        }
    }

    scope.launch {
        sharedFlow.collect {
            println("Collected B - $it")
        }
    }

    Thread.sleep(1500)
}