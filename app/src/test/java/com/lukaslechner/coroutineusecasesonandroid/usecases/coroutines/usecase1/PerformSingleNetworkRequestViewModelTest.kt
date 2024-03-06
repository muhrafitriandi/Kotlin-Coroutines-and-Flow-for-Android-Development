package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class PerformSingleNetworkRequestViewModelTest {

    private val receivedUiState = mutableListOf<UiState>()

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should return Success when network is successful`() {
        // Arrange
        val dispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(dispatcher)

        val fakeApi = FakeSuccessApi()
        val viewModel = PerformSingleNetworkRequestViewModel(fakeApi)

        observeViewModel(viewModel)

        // Act
        viewModel.performSingleNetworkRequest()

        // Assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(mockAndroidVersions)
            ),
            receivedUiState
        )

        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should return Error when network is fails`() {
        // Arrange
        val dispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(dispatcher)

        val fakeApi = FakeErrorApi()
        val viewModel = PerformSingleNetworkRequestViewModel(fakeApi)

        observeViewModel(viewModel)

        // Act
        viewModel.performSingleNetworkRequest()

        // Assert
        val expectedErrorState = receivedUiState.find { it is UiState.Error } as UiState.Error
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error(expectedErrorState.message)
            ),
            receivedUiState
        )

        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    private fun observeViewModel(viewModel: PerformSingleNetworkRequestViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiState.add(uiState)
            }
        }
    }
}