package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14

import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class AndroidVersionRepositoryTest {

    @Test
    fun `getLocalAndroidVersions() should return android versions from database`() = runTest {
        val fakeDatabase = FakeDatabase()

        val repository = AndroidVersionRepository(fakeDatabase, this)
        Assert.assertEquals(mockAndroidVersions, repository.getLocalAndroidVersions())
    }

    @Test
    fun `loadRecentAndroidVersions() should return android versions from network`() = runTest {
        val fakeDatabase = FakeDatabase()
        val fakeApi = FakeApi()
        val repository = AndroidVersionRepository(
            fakeDatabase,
            this,
            fakeApi
        )
        Assert.assertEquals(mockAndroidVersions, repository.loadAndStoreRemoteAndroidVersions())
    }

    @Test
    fun `loadRecentAndroidVersions() should continue to load and store android versions when calling scope gets cancelled`() = runTest {
        val fakeDatabase = FakeDatabase()
        val fakeApi = FakeApi()
        val applicationScope = this
        val repository = AndroidVersionRepository(
            fakeDatabase,
            applicationScope,
            fakeApi
        )

        // Sharing the testScheduler with the applicationScope is important!
        val viewModelScope = TestScope(this.testScheduler)
        val job = viewModelScope.launch {
            repository.loadAndStoreRemoteAndroidVersions()
        }

        // execute coroutine until delay(1) in the fakeApi
        applicationScope.runCurrent()

        // Check if nothing is inserted into the db before we cancel the scope
        Assert.assertEquals(false, fakeDatabase.insertedIntoDb)

        // Cancel the scope and check if it was indeed cancelled
        viewModelScope.cancel()
        Assert.assertEquals(true, job.isCancelled)

        // continue coroutine execution after delay(1) in the fakeApi
        applicationScope.advanceTimeBy(1)
        applicationScope.runCurrent()

        Assert.assertEquals(true, fakeDatabase.insertedIntoDb)
    }
}