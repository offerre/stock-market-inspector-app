package th.offer.mystockmarketapp.domain.repository

import th.offer.mystockmarketapp.domain.model.StockSummaryModel

interface StockSummaryRepositoryImpl {
    suspend fun getStockSummary(
        id: String,
        stockId: Int
    ): StockSummaryModel
}