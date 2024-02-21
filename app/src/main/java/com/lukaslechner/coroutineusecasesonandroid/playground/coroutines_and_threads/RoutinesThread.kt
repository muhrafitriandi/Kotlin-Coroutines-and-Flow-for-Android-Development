package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines_and_threads

import kotlin.concurrent.thread

fun main() {
    println("Program started")
    routines(1, 1000)
    routines(2, 500)
    Thread.sleep(1500)
    println("Program finished")
}

fun routines(number: Int, delay: Long) {
    thread {
        println("Routines start $number || ${Thread.currentThread().name}")
        Thread.sleep(delay)
        println("Routines finished $number || ${Thread.currentThread().name}")
    }
}