package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase8

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class RoomAndCoroutinesViewModel(
    private val api: MockApi,
    private val database: AndroidVersionDao
) : BaseViewModel<UiState>() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        uiState.value = UiState.Error(DataSource.NETWORK, "${throwable.message}")
    }

    fun loadData() {
        uiState.value = UiState.Loading.LoadFromDb

        viewModelScope.launch(handler) {
            val localVersions = database.getAndroidVersions()
            if (localVersions.isEmpty()) {
                uiState.value = UiState.Error(DataSource.DATABASE, "Database Empty!")
            } else {
                uiState.value = UiState.Success(DataSource.DATABASE, localVersions.mapToUiModelList())
            }

            uiState.value = UiState.Loading.LoadFromNetwork
            val networkVersions = api.getRecentAndroidVersions()
            networkVersions.forEach { androidVersion ->
                database.insert(androidVersion.mapToEntity())
            }
            uiState.value = UiState.Success(DataSource.NETWORK, networkVersions)
        }
    }

    fun clearDatabase() {
        viewModelScope.launch(handler) {
            database.clear()
        }
    }
}

enum class DataSource(val dataSourceName: String) {
    DATABASE("Database"),
    NETWORK("Network")
}