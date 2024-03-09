package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.coroutineContext

class FlowUseCase1ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    val currentStockPriceAsLiveData: LiveData<UiState> =
        // Simple
        stockPriceDataSource.latestStockList
            .map { stockList ->
                UiState.Success(stockList) as UiState
            }
            .onStart {
                emit(UiState.Loading)
                Timber.d("The flow started!")
            }
            .onCompletion {
                Timber.d("The flow completed!")
            }.asLiveData()

        // Manual
//        viewModelScope.launch {
//            stockPriceDataSource.latestStockList.collect { stockList ->
//                Timber.d("Received item: ${stockList.first()}")
//                uiState.value = UiState.Success(stockList)
//            }
//        }
}