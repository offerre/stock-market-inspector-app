package th.offer.mystockmarketapp.domain.use_case

import th.offer.mystockmarketapp.domain.model.StockSummaryModel
import th.offer.mystockmarketapp.domain.repository.StockSummaryRepositoryImpl

class GetStockSummaryUseCase(
    private val stockSummaryRepositoryImpl: StockSummaryRepositoryImpl
) {
    suspend fun execute(
        id: String,
        stockId: Int
    ): StockSummaryModel {
        return stockSummaryRepositoryImpl.getStockSummary(
            id = id,
            stockId = stockId
        )
    }
}