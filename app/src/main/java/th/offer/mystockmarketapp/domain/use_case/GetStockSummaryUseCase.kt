package th.offer.mystockmarketapp.domain.use_case

import th.offer.mystockmarketapp.domain.model.StockSummaryModel
import th.offer.mystockmarketapp.domain.repository.StockSummaryRepositoryInterface

class GetStockSummaryUseCase(
    private val stockSummaryRepositoryInterface: StockSummaryRepositoryInterface
) {
    suspend fun execute(
        id: String,
        stockId: Int
    ): StockSummaryModel {
        return stockSummaryRepositoryInterface.getStockSummary(
            id = id,
            stockId = stockId
        )
    }
}