package com.lukaslechner.coroutineusecasesonandroid.playground.flow.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun main() {
    val scope = CoroutineScope(Dispatchers.Default)

    // receiveChannel(scope)
    // stateFlow(scope)
    sharedFlow(scope)

    Thread.sleep(1000)
}

private fun receiveChannel(scope: CoroutineScope): ReceiveChannel<Int> {
    val channel = scope.produce {
        delay(100)
        println("Sending 10")
        send(10)

        delay(100)
        println("Sending 20")
        send(20)

        delay(100)
        println("Sending 30")
        send(30)
    }

    scope.launch {
        channel.consumeEach { receivedValue ->
            println("Consumer1: $receivedValue")
        }
    }

    scope.launch {
        channel.consumeEach { receivedValue ->
            println("Consumer2: $receivedValue")
        }
    }
    return channel
}

private fun stateFlow(scope: CoroutineScope) {
    val stateFlow = MutableStateFlow(0)

    scope.launch {
        delay(100)
        println("Sending 10")
        stateFlow.emit(10)

        delay(100)
        println("Sending 20")
        stateFlow.emit(20)

        delay(100)
        println("Sending 30")
        stateFlow.emit(30)
    }

    scope.launch {
        stateFlow.collect { receivedValue ->
            println("Consumer1: $receivedValue")
        }
    }

    scope.launch {
        stateFlow.collect { receivedValue ->
            println("Consumer2: $receivedValue")
        }
    }
}

private fun sharedFlow(scope: CoroutineScope) {
    val sharedFlow = MutableSharedFlow<Int>()

    scope.launch {
        delay(100)
        println("Sending 10")
        sharedFlow.emit(10)

        delay(100)
        println("Sending 20")
        sharedFlow.emit(20)

        delay(100)
        println("Sending 30")
        sharedFlow.emit(30)
    }

    scope.launch {
        sharedFlow.collect { receivedValue ->
            println("Consumer1: $receivedValue")
        }
    }

    scope.launch {
        sharedFlow.collect { receivedValue ->
            println("Consumer2: $receivedValue")
        }
    }
}