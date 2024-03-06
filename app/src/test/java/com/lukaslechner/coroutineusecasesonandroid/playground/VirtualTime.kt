package com.lukaslechner.coroutineusecasesonandroid.playground

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SystemUnderTest {
    suspend fun functionWithDelay(): Int {
        delay(1000)
        return 42
    }
}

fun CoroutineScope.functionThatStartsNewCoroutine() {
    launch {
        delay(1000)
        println("Coroutine Completed")
    }
}

class TestClass {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `functionWithDelay should return 42`() = runTest {
        val realTimeStart = System.currentTimeMillis()
        val virtualTimeStart = currentTime

        val sut = SystemUnderTest()

        val actual = sut.functionWithDelay()

        Assert.assertEquals(42, actual)

//        functionThatStartsNewCoroutine()
//        testScheduler.apply { advanceTimeBy(1000); runCurrent() }

        val realTimeDuration = System.currentTimeMillis() - realTimeStart
        val virtualTimeDuration = currentTime - virtualTimeStart

        println("Test took $realTimeDuration realTime ms")
        println("Test took $virtualTimeDuration virtualTime ms")
    }
}