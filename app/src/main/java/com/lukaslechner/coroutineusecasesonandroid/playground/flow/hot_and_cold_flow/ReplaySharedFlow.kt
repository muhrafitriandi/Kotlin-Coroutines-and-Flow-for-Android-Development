package com.lukaslechner.coroutineusecasesonandroid.playground.flow.hot_and_cold_flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking<Unit> {
    val flow = flow {
        delay(1000)
        emit(1)
        delay(1000)
        emit(2)
        delay(1000)
        emit(3)
        delay(1000)
        emit(4)
        delay(1000)
        emit(5)
    }.shareIn(this, SharingStarted.WhileSubscribed(), replay = 2) // replay = 2 akan memainkan 2 item terakhir ke subscriber baru

    // Membuat beberapa subscriber
    val job1 = launch {
        flow.collect { println("Subscriber 1: $it") }
    }
    delay(4500) // Memberi jeda agar subscriber 1 bisa menerima item pertama, kedua, ketiga, keempat

    val job2 = launch {
        flow.collect { println("Subscriber 2: $it") }
    }

    delay(6000)

    job1.cancel()
    job2.cancel()
}

//Subscriber 1: 1 (Subscriber 1 menerima item pertama)
//Subscriber 1: 2 (Subscriber 1 menerima item kedua)
//Subscriber 1: 3 (Subscriber 1 menerima item ketiga)
//Subscriber 1: 4 (Subscriber 1 menerima item keempat)
//Subscriber 2: 3 (Subscriber 2 baru berlangganan, maka item 2 terakhir yaitu 3, 4 dimainkan kembali kepadanya
//Subscriber 2: 4
//Subscriber 1: 5 (Subscriber 1 menerima item terakhir yang dimainkan kembali)
//Subscriber 2: 5 (Subscriber 2 menerima item terakhir yang dimainkan kembali)

