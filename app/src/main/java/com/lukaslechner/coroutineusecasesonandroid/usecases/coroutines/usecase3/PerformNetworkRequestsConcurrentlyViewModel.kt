package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineExceptionHandler
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

            uiState.value = UiState.Success(listOf(androidOreo, androidPie, android10))
        }
    }

    fun performNetworkRequestsConcurrently() {

    }
}