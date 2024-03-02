package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber

class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        uiState.value = UiState.Error("Error: ${throwable.message}")
    }

    fun performSingleNetworkRequest() {
        uiState.value = UiState.Loading

        val job = viewModelScope.launch(handler) {
            Timber.d("I am the first statement in the coroutine")
            val androidVersion = mockApi.getRecentAndroidVersions()
            uiState.value = UiState.Success(androidVersion)
        }

        Timber.d("I am the first statement after launching the coroutine")

        job.invokeOnCompletion { throwable ->
            if (throwable is CancellationException) {
                Timber.d("Coroutine was cancelled")
            }
        }
    }
}