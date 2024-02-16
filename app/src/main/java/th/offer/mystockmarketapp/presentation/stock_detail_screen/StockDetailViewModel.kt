package th.offer.mystockmarketapp.presentation.stock_detail_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import th.offer.mystockmarketapp.domain.use_case.GetChartDetailUseCase
import th.offer.mystockmarketapp.domain.use_case.GetStockSummaryUseCase
import th.offer.mystockmarketapp.navigation.ID
import th.offer.mystockmarketapp.navigation.STOCK_ID
import javax.inject.Inject

@HiltViewModel
class StockDetailViewModel @Inject constructor(
    private val getStockSummaryUseCase: GetStockSummaryUseCase,
    private val getChartDetailUseCase: GetChartDetailUseCase,
    savedStateHandle: SavedStateHandle?
) : ViewModel() {
    val id = savedStateHandle?.get<String>(ID)
    val stockId = savedStateHandle?.get<Int>(STOCK_ID)

    private val _state = MutableStateFlow(
        StockDetailState(
            id = id ?: "",
            stockId = stockId ?: -1
        )
    )
    val state = _state.asStateFlow()

    init {
        fetchStockDetailData()
    }

    fun fetchStockDetailData() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            _state.update {
                it.copy(
                    stockSummaryModel = getStockSummaryUseCase.execute(
                        id = _state.value.id,
                        stockId = _state.value.stockId
                    ),
                    stockChartDetailModel = getChartDetailUseCase.execute(
                        id = _state.value.id,
                        stockId = _state.value.stockId,
                    ),
                    isLoading = false
                )
            }
        }
    }
}