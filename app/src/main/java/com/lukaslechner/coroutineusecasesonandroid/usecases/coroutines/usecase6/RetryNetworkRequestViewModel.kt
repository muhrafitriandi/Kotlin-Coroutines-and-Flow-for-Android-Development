package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Timber.d("${throwable.message}")
        uiState.value = UiState.Error("Error: ${throwable.message}")
    }

    fun performNetworkRequestVer1() {
        uiState.value = UiState.Loading
        val number0fRetries = 3

        viewModelScope.launch(handler) {
            // retry manually
            repeat(number0fRetries) {
                try {
                    loadRecentAndroidVersions()
                    return@launch // We need this to exit the coroutine if the response is successful.
                    // So that when the response is successful it immediately exits the `launch` code block so that other code will not be executed.
                } catch (e: Exception) {
                    Timber.d("${e.message}")
                }
            }
            loadRecentAndroidVersions()
            Timber.d("Finished.")
        }
    }

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        val number0fRetries = 2

        viewModelScope.launch(handler) {
            retryComplexWithExponentialBackoff(number0fRetries) {
                loadRecentAndroidVersions()
            }
        }
    }

    private suspend fun <T> retrySimple(numberOfRetries: Int, block: suspend () -> T): T {
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (e: Exception) {
                Timber.d("${e.message}")
            }
        }
        Timber.d("Finished.")
        return block()
    }

    private suspend fun <T> retryComplexWithExponentialBackoff(
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
            Timber.d("Initial Delay: $currentDelay")
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
            Timber.d("Current Delay: $currentDelay")
        }
        Timber.d("Finished.")
        return block()
    }

    private suspend fun loadRecentAndroidVersions() {
        val androidVersion = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(androidVersion)
    }
}