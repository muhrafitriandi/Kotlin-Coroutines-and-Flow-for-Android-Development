package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

class NetworkRequestWithTimeoutViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        uiState.value = UiState.Error("Error: ${throwable.message}")
    }

    fun performNetworkRequest(timeout: Long) {
        uiState.value = UiState.Loading

        // usingWithTimeOut(timeout)
        usingWithTimeOutOrNull(timeout)
    }

    private fun usingWithTimeOut(timeout: Long) {
        viewModelScope.launch(handler) {
            try {
                val androidVersion = withTimeout(timeout) {
                    api.getRecentAndroidVersions()
                }
                uiState.value = UiState.Success(androidVersion)
            } catch (e: TimeoutCancellationException) {
                uiState.value = UiState.Error("Request time out!")
            }
        }
    }

    private fun usingWithTimeOutOrNull(timeout: Long) {
        viewModelScope.launch(handler) {
            val androidVersion = withTimeoutOrNull(timeout) {
                api.getRecentAndroidVersions()
            }

            if (androidVersion != null) {
                uiState.value = UiState.Success(androidVersion)
            } else {
                uiState.value = UiState.Error("Request time out!")
            }
        }
    }
}