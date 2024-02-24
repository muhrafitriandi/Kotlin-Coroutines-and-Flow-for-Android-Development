package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Timber.d("${throwable.message}")
        uiState.value = UiState.Error("Error: ${throwable.message}")
    }

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        val number0fRetries = 3

        viewModelScope.launch(handler) {
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

    private suspend fun loadRecentAndroidVersions() {
        val androidVersion = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(androidVersion)
    }
}