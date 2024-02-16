package th.offer.mystockmarketapp.presentation.stock_detail_screen

import th.offer.mystockmarketapp.domain.model.StockChartDetailModel
import th.offer.mystockmarketapp.domain.model.StockSummaryModel

data class StockDetailState(
    var isLoading: Boolean = false,
    var id: String,
    var stockId: Int,
    var stockSummaryModel: StockSummaryModel? = null,
    var stockChartDetailModel: StockChartDetailModel? = null,
)
