package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber

class TimeoutAndRetryViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        uiState.value = UiState.Error("Error: ${throwable.message}")
    }

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        val numberOfRetries = 2
        val timeout = 1000L

        // TODO: Exercise 3
        // switch to branch "coroutine_course_full" to see solution

        // run api.getAndroidVersionFeatures(27) and api.getAndroidVersionFeatures(28) in parallel
        val deferredAndroidOreo = viewModelScope.async {
            retryWithTimeout(numberOfRetries, timeout) {
                api.getAndroidVersionFeatures(27)
            }
        }
        val deferredAndroidPie = viewModelScope.async {
            retryWithTimeout(numberOfRetries, timeout) {
                api.getAndroidVersionFeatures(28)
            }
        }

        viewModelScope.launch(handler) {
            val versionFeatures = awaitAll(deferredAndroidOreo, deferredAndroidPie)
            uiState.value = UiState.Success(versionFeatures)
        }
    }

    private suspend fun <T> retryWithTimeout(
        numberOfRetries: Int,
        timeout: Long,
        block: suspend () -> T
    ) = retry(numberOfRetries) {
        withTimeout(timeout) {
            block()
        }
    }

    private suspend fun <T> retry(
        numberOfRetries: Int,
        initialDelayMillis: Long = 100,
        maxDelayMillis: Long = 1000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelayMillis
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (e: Exception) {
                Timber.d("${e.message}")
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor.toLong()).coerceAtMost(maxDelayMillis)
        }
        return block()
    }
}