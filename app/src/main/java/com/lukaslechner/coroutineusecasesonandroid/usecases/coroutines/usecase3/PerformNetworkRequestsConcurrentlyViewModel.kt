package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        uiState.value = UiState.Error("Error: ${throwable.message}")
    }

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading

        viewModelScope.launch(handler) {
            val androidOreo = mockApi.getAndroidVersionFeatures(27)
            val androidPie = mockApi.getAndroidVersionFeatures(28)
            val android10 = mockApi.getAndroidVersionFeatures(29)

            val versionFeatures = listOf(androidOreo, androidPie, android10)
            uiState.value = UiState.Success(versionFeatures)
        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading

        val deferredAndroidOreo = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(27)
        }
        val deferredAndroidPie = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(28)
        }
        val deferredAndroid10 = viewModelScope.async {
            mockApi.getAndroidVersionFeatures(29)
        }

        viewModelScope.launch(handler) {
            val versionFeatures = awaitAll(deferredAndroidOreo, deferredAndroidPie, deferredAndroid10)
            uiState.value = UiState.Success(versionFeatures)
        }
    }
}