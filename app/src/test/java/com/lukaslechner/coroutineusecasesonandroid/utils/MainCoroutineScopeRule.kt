package com.lukaslechner.coroutineusecasesonandroid.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.rules.TestWatcher
import org.junit.runner.Description

// Option 1: Inheritance
//@OptIn(ExperimentalCoroutinesApi::class)
//open class MainCoroutineScopeRule {
//
//    private val dispatcher = TestCoroutineDispatcher()
//
//    @Before
//    fun setUp() {
//        Dispatchers.setMain(dispatcher)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//        dispatcher.cleanupTestCoroutines()
//    }
//}

// Option 2: TestWatcher
@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineScopeRule(
    private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }
}