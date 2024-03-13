package com.lukaslechner.coroutineusecasesonandroid.playground.flow.hot_and_cold_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
fun main() {
    val sharedFlow = MutableSharedFlow<Int>()
    val stateFlow = MutableStateFlow(0)
    val scope = CoroutineScope(Dispatchers.Default)

    // Shared Flow
    scope.launch {
        delay(200)
        sharedFlow.emit(1)
        delay(200)
        sharedFlow.emit(1)
        delay(200)
        sharedFlow.emit(2)
        delay(200)
        sharedFlow.emit(1)
    }
    scope.launch {
        sharedFlow.collect {
            println("Received value - Shared Flow: $it")
        }
    }
    Thread.sleep(1500)

    // State Flow
    scope.launch {
        delay(200)
        stateFlow.emit(1)
        delay(200)
        stateFlow.emit(1)
        delay(200)
        stateFlow.emit(2)
        delay(200)
        stateFlow.emit(1)
    }
    scope.launch {
        stateFlow.collect {
            println("Received value - State Flow: $it")
        }
    }
    Thread.sleep(1500)
}