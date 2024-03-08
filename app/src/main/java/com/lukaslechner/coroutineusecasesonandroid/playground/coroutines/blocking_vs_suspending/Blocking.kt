package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines.blocking_vs_suspending

import kotlin.concurrent.thread

fun main() {
    println("Program started")
    routines(1, 1000)
    routines(2, 500)
    thread {
        repeat(5) {
            println("${it + 1} || ${Thread.currentThread().name}")
            Thread.sleep(250)
        }
    }
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