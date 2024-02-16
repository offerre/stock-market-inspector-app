package th.offer.mystockmarketapp.domain.repository

import th.offer.mystockmarketapp.domain.model.StockChartDetailModel

interface StockChartDetailRepositoryInterface {
    suspend fun getChartDetail(
        id: String,
        stockId: Int
    ): StockChartDetailModel
}