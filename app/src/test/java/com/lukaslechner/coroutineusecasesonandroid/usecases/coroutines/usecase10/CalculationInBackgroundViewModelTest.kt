package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule

import org.junit.Test

@ExperimentalCoroutinesApi
class CalculationInBackgroundViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScopeRule = MainCoroutineScopeRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performCalculation() should perform correct calculations`() = runTest {
        // Arrange
        val viewModel = CalculationInBackgroundViewModel(StandardTestDispatcher(testScheduler))
        observeViewModel(viewModel)
        
        // Act
        viewModel.performCalculation(1)
        runCurrent()

        // Assert
        Assert.assertEquals(
            UiState.Loading,
            receivedUiStates.first()
        )
        Assert.assertEquals(
            "1",
            (receivedUiStates[1] as UiState.Success).result
        )
        receivedUiStates.clear()

        viewModel.performCalculation(2)
        runCurrent()

        Assert.assertEquals(
            UiState.Loading,
            receivedUiStates.first()
        )
        Assert.assertEquals(
            "2",
            (receivedUiStates[1] as UiState.Success).result
        )
        receivedUiStates.clear()

        viewModel.performCalculation(3)
        runCurrent()

        Assert.assertEquals(
            UiState.Loading,
            receivedUiStates.first()
        )
        Assert.assertEquals(
            "6",
            (receivedUiStates[1] as UiState.Success).result
        )
    }

    private fun observeViewModel(viewModel: CalculationInBackgroundViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}