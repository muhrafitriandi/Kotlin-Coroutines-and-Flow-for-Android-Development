package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.ReplaceMainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule

import org.junit.Test

@ExperimentalCoroutinesApi
class NetworkRequestWithTimeoutViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performNetworkRequest() should return Success UiState on successful network request within timeout`() =
        runTest {
            // Arrange
            val responseDelay = 1000L
            val timeout = 1001L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = NetworkRequestWithTimeoutViewModel(fakeApi)
            observeViewModel(viewModel)

            // Act
            viewModel.performNetworkRequest(timeout)
            advanceUntilIdle()

            // Assert
            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(mockAndroidVersions)
                ),
                receivedUiStates
            )

            Assert.assertEquals(
                1000, currentTime
            )
        }

    @Test
    fun `performNetworkRequest() should return Error UiState with timeout error message if timeout gets exceeded`() =
        runTest {
            // Arrange
            val responseDelay = 1000L
            val timeout = 999L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = NetworkRequestWithTimeoutViewModel(fakeApi)
            observeViewModel(viewModel)

            // Act
            viewModel.performNetworkRequest(timeout)
            advanceUntilIdle()

            // Assert
            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Request time out!")
                ),
                receivedUiStates
            )
        }

    @Test
    fun `performNetworkRequest() should return Error UiState on unsuccessful network response`() =
        runTest {
            // Arrange
            val responseDelay = 1000L
            val timeout = 1001L
            val fakeApi = FakeErrorApi(responseDelay)
            val viewModel = NetworkRequestWithTimeoutViewModel(fakeApi)
            observeViewModel(viewModel)

            // Act
            viewModel.performNetworkRequest(timeout)
            advanceUntilIdle()

            // Assert
            val expectedErrorState = receivedUiStates.find { it is UiState.Error } as UiState.Error
            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error(expectedErrorState.message)
                ),
                receivedUiStates
            )
        }

    private fun observeViewModel(viewModel: NetworkRequestWithTimeoutViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}