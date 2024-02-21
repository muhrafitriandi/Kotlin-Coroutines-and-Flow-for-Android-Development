package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.d("3. Running on thread: ${Thread.currentThread().name}")
        viewModelScope.launch(Dispatchers.Main) {
            Timber.d("4. Running on thread: ${Thread.currentThread().name}")
            uiState.value = UiState.Error("Error: ${throwable.message.toString()}")
        }
    }

    private lateinit var job: Job

    fun perform2SequentialNetworkRequest() {
        // TODO: Exercise 1
        // switch to branch "coroutine_course_full" to see solution
        uiState.value = UiState.Loading

        job = viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            Timber.d("1. Running on thread: ${Thread.currentThread().name}")
            val androidVersion = mockApi.getRecentAndroidVersions().last()
            val versionFeatures = mockApi.getAndroidVersionFeatures(androidVersion.apiLevel)

            withContext(Dispatchers.Main) {
                Timber.d("2. Running on thread: ${Thread.currentThread().name}")
                Timber.d("Result: $versionFeatures")
                uiState.value = UiState.Success(versionFeatures)
            }
        }

        Timber.d("Status Active Job Before onCleared(): ${job.isActive}")
        Timber.d("Status Cancelled Job Before onCleared(): ${job.isCancelled}")
    }

    override fun onCleared() {
        super.onCleared()

        Timber.d("Status Active Job After onCleared(): ${job.isActive}") // Will return false if the coroutine has finished executing regardless of success/failure.
        Timber.d("Status Cancelled Job After onCleared(): ${job.isCancelled}") // Will return true if the network request process is not yet complete but the user has left the screen.
    }
}