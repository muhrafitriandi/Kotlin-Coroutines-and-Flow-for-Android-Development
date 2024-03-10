package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase2

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.withIndex
import timber.log.Timber

class FlowUseCase2ViewModel(
    stockPriceDataSource: StockPriceDataSource,
    defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<UiState>() {

    /*

    Flow exercise 1 Goals
        1) only update stock list when Alphabet(Google) (stock.name ="Alphabet (Google)") stock price is > 2300$
        2) only show stocks of "United States" (stock.country == "United States")
        3) show the correct rank (stock.rank) within "United States", not the world wide rank
        4) filter out Apple  (stock.name ="Apple") and Microsoft (stock.name ="Microsoft"), so that Google is number one
        5) only show company if it is one of the biggest 10 companies of the "United States" (stock.rank <= 10)
        6) stop flow collection after 10 emissions from the dataSource
        7) log out the number of the current emission so that we can check if flow collection stops after exactly 10 emissions
        8) Perform all flow processing on a background thread

     */

    val currentStockPriceAsLiveData: LiveData<UiState> = stockPriceDataSource.latestStockList
        // 7) log out the number of the current emission so that we can check if flow collection stops after exactly 10 emissions
        .withIndex()
        .onEach { indexedValue ->
            Timber.d("Processing emission: ${indexedValue.index + 1}")
        }
        .map { indexedValue ->
            indexedValue.value
        }
        .take(10) // 6) stop flow collection after 10 emissions from the dataSource
        // 1) only update stock list when Alphabet(Google) (stock.name ="Alphabet (Google)") stock price is > 2300$
        .filter { stockList ->
            val googlePrice = stockList.find { stock ->
                stock.name == "Alphabet (Google)"
            }?.currentPrice ?: return@filter false

            googlePrice > 2300
        }
        // 2) only show stocks of "United States" (stock.country == "United States")
        .map { stockList ->
            stockList.filter { stock ->
                stock.country == "United States"
            }
        }
        // 4) filter out Apple  (stock.name ="Apple") and Microsoft (stock.name ="Microsoft"), so that Google is number one
        .map { stockList ->
            stockList.filter { stock ->
                stock.name != "Apple" && stock.name != "Microsoft"
            }
        }
        // 3) show the correct rank (stock.rank) within "United States", not the world wide rank
        .map { stockList ->
            stockList.mapIndexed { index, stock ->
                stock.copy(rank = index + 1)
            }
        }
        // 5) only show company if it is one of the biggest 10 companies of the "United States" (stock.rank <= 10)
        .map { stockList ->
            stockList.filter { stock ->
                stock.rank <= 10
            }
        }
        .map { stockList ->
            UiState.Success(stockList) as UiState
        }
        .onStart {
            emit(UiState.Loading)
        }
        .onCompletion {
            Timber.d("Flow has completed!")
        }
        .asLiveData(defaultDispatcher) // 8) Perform all flow processing on a background thread


}