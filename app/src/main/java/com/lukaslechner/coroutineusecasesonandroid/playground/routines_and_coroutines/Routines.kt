package com.lukaslechner.coroutineusecasesonandroid.playground.routines_and_coroutines

fun main() {
    println("Program started")
    routines(1, 1000)
    routines(2, 500)
    println("Program finished")
}

fun routines(number: Int, delay: Long) {
    println("Routines start $number")
    Thread.sleep(delay)
    println("Routines finished $number")
}