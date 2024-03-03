package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import timber.log.Timber
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CooperativeCancellationViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private var calculationJob: Job? = null

    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading

        calculationJob = viewModelScope.launch(defaultDispatcher) {
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

        calculationJob?.invokeOnCompletion { throwable ->
            if (throwable is CancellationException) {
                Timber.d("Coroutine was cancelled")
                viewModelScope.launch {
                    withContext(Dispatchers.Main) {
                        uiState.value = UiState.Error(throwable.message.toString())
                    }
                }
            }
        }
    }

    private suspend fun calculateFactorialOf(number: Int): BigInteger {
        var factorial = BigInteger.ONE
        for (i in 1..number) {
            yield()
            factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
        }
        Timber.d("Calculating Factorial Completed!")
        return factorial
    }

    fun cancelCalculation() {
        calculationJob?.cancel()
    }

    fun uiState(): LiveData<UiState> = uiState

    private val uiState: MutableLiveData<UiState> = MutableLiveData()
}