package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule

import org.junit.Test

@ExperimentalCoroutinesApi
class RetryNetworkRequestViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScopeRule = MainCoroutineScopeRule()

    private val receiveUiState = mutableListOf<UiState>()

    @Test
    fun `performSingleNetworkRequest() should return Success UiState on successful network response`() =
        runTest {
            // Arrange
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = RetryNetworkRequestViewModel(fakeApi)
            observeViewModel(viewModel)

            // Act
            viewModel.performNetworkRequest()
            advanceUntilIdle()

            // Assert
            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(mockAndroidVersions)
                ),
                receiveUiState
            )

            Assert.assertEquals(
                1000, currentTime
            )
        }

    @Test
    fun `performSingleNetworkRequest() should retry network request two times`() = runTest {
        // Arrange
        val responseDelay = 1000L
        val fakeApi = FakeSuccessOnThirdAttemptApi(responseDelay)
        val viewModel = RetryNetworkRequestViewModel(fakeApi)
        observeViewModel(viewModel)

        // Act
        viewModel.performNetworkRequest()
        advanceUntilIdle()

        // Assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(mockAndroidVersions)
            ),
            receiveUiState
        )

        Assert.assertEquals(
            3,
            fakeApi.requestCount
        )

        // 3*1000 (Request delays) + 100 (initial delay) + 200 (second delay)
        Assert.assertEquals(
            3300,
            currentTime
        )
    }

    @Test
    fun `performSingleNetworkRequest() should return Error UiState on 3 unsuccessful network responses`() =
        runTest {
            // Arrange
            val responseDelay = 1000L
            val fakeApi = FakeErrorApi(responseDelay)
            val viewModel = RetryNetworkRequestViewModel(fakeApi)
            observeViewModel(viewModel)

            // Act
            viewModel.performNetworkRequest()
            advanceUntilIdle()

            // Assert
            val expectedErrorState = receiveUiState.find { it is UiState.Error } as UiState.Error
            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error(expectedErrorState.message)
                ),
                receiveUiState
            )

            Assert.assertEquals(
                3,
                fakeApi.requestCount
            )

            // 3*1000 response delays + 100 (initial delay) + 200 (second delay)
            Assert.assertEquals(
                3300,
                currentTime
            )
        }

    private fun observeViewModel(viewModel: RetryNetworkRequestViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receiveUiState.add(uiState)
            }
        }
    }
}