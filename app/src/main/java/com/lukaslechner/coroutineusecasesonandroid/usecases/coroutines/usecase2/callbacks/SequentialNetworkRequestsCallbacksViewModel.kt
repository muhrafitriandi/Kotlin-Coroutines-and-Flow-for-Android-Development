package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private var getRecentAndroidVersionsCall: Call<List<AndroidVersion>>? = null
    private var getAndroidVersionFeaturesCall: Call<VersionFeatures>? = null

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        getRecentAndroidVersionsCall = mockApi.getRecentAndroidVersions()
        getRecentAndroidVersionsCall?.enqueue(object : Callback<List<AndroidVersion>> {
            override fun onResponse(
                call: Call<List<AndroidVersion>>,
                response: Response<List<AndroidVersion>>
            ) {
                if (response.isSuccessful) {
                    val androidVersion = response.body()?.last()
                    Timber.d("androidVersionResponse: $androidVersion")

                    androidVersion?.let {
                        getAndroidVersionFeaturesCall = mockApi.getAndroidVersionFeatures(androidVersion.apiLevel)
                        getAndroidVersionFeaturesCall?.enqueue(object : Callback<VersionFeatures> {
                            override fun onResponse(
                                call: Call<VersionFeatures>,
                                response: Response<VersionFeatures>
                            ) {
                                if (response.isSuccessful) {
                                    val versionFeatures = response.body()

                                    versionFeatures?.let {
                                        Timber.d("versionFeaturesResponse: $versionFeatures")
                                        uiState.value = UiState.Success(versionFeatures)
                                    }
                                } else {
                                    uiState.value = UiState.Error(response.message())
                                }
                            }

                            override fun onFailure(
                                call: Call<VersionFeatures>,
                                t: Throwable
                            ) {
                                uiState.value = UiState.Error(t.message.toString())
                            }
                        })
                    }
                } else {
                    uiState.value = UiState.Error(response.message())
                }
            }

            override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                uiState.value = UiState.Error(t.message.toString())
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("Status getRecentAndroidVersions() Before Cancel: ${getRecentAndroidVersionsCall?.isCanceled}")
        Timber.d("Status getAndroidVersionFeatures() Before Cancel: ${getAndroidVersionFeaturesCall?.isCanceled}")

        getRecentAndroidVersionsCall?.cancel()
        getAndroidVersionFeaturesCall?.cancel()

        Timber.d("Status getRecentAndroidVersions() After Cancel: ${getRecentAndroidVersionsCall?.isCanceled}")
        Timber.d("Status getAndroidVersionFeatures() After Cancel: ${getAndroidVersionFeaturesCall?.isCanceled}")
    }
}