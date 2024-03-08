package com.lukaslechner.coroutineusecasesonandroid.playground.coroutines.how_delay_works

import android.os.Handler
import android.os.Looper

fun main() {
    println("Program started")
    coroutines(1, 1000)
    coroutines(2, 500)
    println("Program finished")
}

fun coroutines(number: Int, delay: Long) {
    println("Coroutines start $number")
    // delay(delay)

    // will error, handler will run in main thread
    // because, for regular kotlin playground it doesn't have a main thread
    Handler(Looper.getMainLooper()).postDelayed({
        println("Coroutines finished $number")
    }, delay)
}