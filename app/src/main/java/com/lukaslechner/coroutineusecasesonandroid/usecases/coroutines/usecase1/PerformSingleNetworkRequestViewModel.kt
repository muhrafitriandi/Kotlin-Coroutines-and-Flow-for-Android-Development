package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        uiState.value = UiState.Error("Error: ${throwable.message}")
    }

    fun performSingleNetworkRequest() {
        uiState.value = UiState.Loading

        viewModelScope.launch(handler) {
            val androidVersion = mockApi.getRecentAndroidVersions()
            uiState.value = UiState.Success(androidVersion)
        }
    }
}