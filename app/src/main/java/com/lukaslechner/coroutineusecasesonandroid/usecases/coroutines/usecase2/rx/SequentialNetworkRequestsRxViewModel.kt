package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.rx

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SequentialNetworkRequestsRxViewModel(
    private val mockApi: RxMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private var disposables = CompositeDisposable()

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        mockApi.getRecentAndroidVersions()
            .flatMap { androidVersions ->
                val androidVersion = androidVersions.last()
                mockApi.getAndroidVersionFeatures(androidVersion.apiLevel)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { versionFeatures ->
                    Timber.d("Observe Data Running on: ${Thread.currentThread().name}")
                    uiState.value = UiState.Success(versionFeatures)
                },
                onError = {
                    uiState.value = UiState.Error(it.message.toString())
                }
            ).addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()

        Timber.d("Status Disposables Before Clear: ${disposables.isDisposed}")
        disposables.dispose()
        Timber.d("Status Disposables After Clear: ${disposables.isDisposed}")
    }
}