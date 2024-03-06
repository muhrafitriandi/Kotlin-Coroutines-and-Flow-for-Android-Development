package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import org.junit.Assert
import org.junit.Rule

import org.junit.Test

class Perform2SequentialNetworkRequestsViewModelTest {

    private val receivedUiState = mutableListOf<UiState>()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScopeRule = MainCoroutineScopeRule()

    @Test
    fun `should return Success when network is success`() {
        // Arrange
        val fakeApi = FakeSuccessApi()
        val viewModel = Perform2SequentialNetworkRequestsViewModel(fakeApi)

        observeViewModel(viewModel)

        // Act
        viewModel.perform2SequentialNetworkRequest()

        // Assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(mockVersionFeaturesAndroid10)
            ),
            receivedUiState
        )
    }

    @Test
    fun `should return Error when androidVersions is fail`() {
        // Arrange
        val fakeApi = FakeAndroidVersionsErrorApi()
        val viewModel = Perform2SequentialNetworkRequestsViewModel(fakeApi)

        observeViewModel(viewModel)

        // Act
        viewModel.perform2SequentialNetworkRequest()

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

    @Test
    fun `should return Error when versionFeatures is fail`() {
        // Arrange
        val fakeApi = FakeVersionFeaturesErrorApi()
        val viewModel = Perform2SequentialNetworkRequestsViewModel(fakeApi)

        observeViewModel(viewModel)

        // Act
        viewModel.perform2SequentialNetworkRequest()

        // Assert
        val expectedErrorState = receivedUiState.find { it is UiState.Error } as UiState.Error
        println(expectedErrorState.message)
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error(expectedErrorState.message)
            ),
            receivedUiState
        )
    }

    private fun observeViewModel(viewModel: Perform2SequentialNetworkRequestsViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiState.add(uiState)
            }
        }
    }
}