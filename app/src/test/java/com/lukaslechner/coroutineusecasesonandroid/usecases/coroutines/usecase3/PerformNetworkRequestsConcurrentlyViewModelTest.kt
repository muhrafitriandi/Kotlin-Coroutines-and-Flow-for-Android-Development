package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesOreo
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesPie
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule

import org.junit.Test

class PerformNetworkRequestsConcurrentlyViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScopeRule = MainCoroutineScopeRule()

    private val receivedUiState = mutableListOf<UiState>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `performNetworkRequestsSequentially should load data sequentially`() = runTest {
        // Arrange
        val responseDelay = 1000L
        val fakeApi = FakeSuccessApi(responseDelay)
        val viewModel = PerformNetworkRequestsConcurrentlyViewModel(fakeApi)

        observeViewModel(viewModel)

        // Act
        viewModel.performNetworkRequestsSequentially()
        advanceUntilIdle()

        // Assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(
                    listOf(
                        mockVersionFeaturesOreo,
                        mockVersionFeaturesPie,
                        mockVersionFeaturesAndroid10
                    )
                )
            ),
            receivedUiState
        )

        // Verify that requests actually got executed sequentially and it took 3000ms to receive all data
        Assert.assertEquals(3000, currentTime)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `performNetworkRequestsConcurrently should load data concurrently`() = runTest {
        // Arrange
        val responseDelay = 1000L
        val fakeApi = FakeSuccessApi(responseDelay)
        val viewModel = PerformNetworkRequestsConcurrentlyViewModel(fakeApi)

        observeViewModel(viewModel)

        // Act
        viewModel.performNetworkRequestsConcurrently()
        advanceUntilIdle()

        // Assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(
                    listOf(
                        mockVersionFeaturesOreo,
                        mockVersionFeaturesPie,
                        mockVersionFeaturesAndroid10
                    )
                )
            ),
            receivedUiState
        )

        // Verify that requests actually got executed concurrently within 1000ms
        Assert.assertEquals(1000, currentTime)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `performNetworkRequestsConcurrently should return Error when network request fails`() = runTest {
        // Arrange
        val responseDelay = 1000L
        val fakeApi = FakeErrorApi(responseDelay)
        val viewModel = PerformNetworkRequestsConcurrentlyViewModel(fakeApi)

        observeViewModel(viewModel)

        // Act
        viewModel.performNetworkRequestsConcurrently()
        advanceUntilIdle()

        // Assert
        val expectedErrorState = receivedUiState.find { it is UiState.Error } as UiState.Error
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error(expectedErrorState.message)
            ),
            receivedUiState
        )
    }

    private fun observeViewModel(viewModel: PerformNetworkRequestsConcurrentlyViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiState.add(uiState)
            }
        }
    }
}