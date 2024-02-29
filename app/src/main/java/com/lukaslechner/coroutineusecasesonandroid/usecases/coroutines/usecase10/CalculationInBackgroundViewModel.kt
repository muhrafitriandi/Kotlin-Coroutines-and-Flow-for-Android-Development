package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CalculationInBackgroundViewModel : BaseViewModel<UiState>() {

    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.Default) {
            var result: BigInteger
            val computationDuration = measureTimeMillis {
                result = calculateFactorialOf(factorialOf)
            }

            var resultString: String
            val stringComputationDuration = measureTimeMillis {
                resultString = result.toString()
            }

            withContext(Dispatchers.Main) {
                uiState.value = UiState.Success(resultString, computationDuration, stringComputationDuration)
            }
        }
    }

    private fun calculateFactorialOf(number: Int) = (1..number).fold(
        BigInteger.ONE
    ) { acc, i ->
        acc.multiply(BigInteger.valueOf(i.toLong()))
    }
}