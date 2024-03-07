package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

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

class VariableAmountOfNetworkRequestsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScopeRule = MainCoroutineScopeRule()

    private val receivedUiState = mutableListOf<UiState>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun performNetworkRequestsSequentially() = runTest {
        // Arrange
        val responseDelay = 1000L
        val fakeApi = FakeSuccessApi(responseDelay)
        val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)
        observeViewModel(viewModel)

        // Act
        viewModel.performNetworkRequestsSequentially()
        advanceUntilIdle()

        // Assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(listOf(mockVersionFeaturesOreo, mockVersionFeaturesPie, mockVersionFeaturesAndroid10))
            ),
            receivedUiState
        )

        Assert.assertEquals(
            4000, currentTime
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun performNetworkRequestsConcurrently() = runTest {
        // Arrange
        val responseDelay = 1000L
        val fakeApi = FakeSuccessApi(responseDelay)
        val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)
        observeViewModel(viewModel)

        // Act
        viewModel.performNetworkRequestsConcurrently()
        advanceUntilIdle()

        // Assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(listOf(mockVersionFeaturesOreo, mockVersionFeaturesPie, mockVersionFeaturesAndroid10))
            ),
            receivedUiState
        )

        Assert.assertEquals(
            2000, currentTime
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `performNetworkRequestsSequentially() should return Error UiState on unsuccessful android-version-features network request`() = runTest {
        // Arrange
        val responseDelay = 1000L
        val fakeApi = FakeAndroidVersionsErrorApi(responseDelay)
        val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)
        observeViewModel(viewModel)

        // Act
        viewModel.performNetworkRequestsSequentially()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `performNetworkRequestsConcurrently() should return Error UiState on unsuccessful android-version-features network request`() = runTest {
        // Arrange
        val responseDelay = 1000L
        val fakeApi = FakeAndroidVersionsErrorApi(responseDelay)
        val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `performNetworkRequestsSequentially() should return Error UiState on unsuccessful recent-android-versions network request`() = runTest {
        // Arrange
        val responseDelay = 1000L
        val fakeApi = FakeVersionFeaturesErrorApi(responseDelay)
        val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)
        observeViewModel(viewModel)

        // Act
        viewModel.performNetworkRequestsSequentially()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `performNetworkRequestsConcurrently() should return Error UiState on unsuccessful recent-android-versions network request`() = runTest {
        // Arrange
        val responseDelay = 1000L
        val fakeApi = FakeVersionFeaturesErrorApi(responseDelay)
        val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)
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

    private fun observeViewModel(viewModel: VariableAmountOfNetworkRequestsViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiState.add(uiState)
            }
        }
    }
}