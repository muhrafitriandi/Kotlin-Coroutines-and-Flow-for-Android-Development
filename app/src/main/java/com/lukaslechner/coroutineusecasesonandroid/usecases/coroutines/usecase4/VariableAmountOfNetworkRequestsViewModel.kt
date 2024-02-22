package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class VariableAmountOfNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        uiState.value = UiState.Error("Error: ${throwable.message}")
    }

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading

        viewModelScope.launch(handler) {
            val versionFeatures = mockApi.getRecentAndroidVersions().map { androidVersion ->
                mockApi.getAndroidVersionFeatures(androidVersion.apiLevel)
            }

            uiState.value = UiState.Success(versionFeatures)
        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading

        viewModelScope.launch(handler) {
            val versionFeatures = mockApi.getRecentAndroidVersions().map { androidVersion ->
                async {
                    mockApi.getAndroidVersionFeatures(androidVersion.apiLevel)
                }//.await() // will not concurrently
            }.awaitAll()
            uiState.value = UiState.Success(versionFeatures)
        }
    }
}