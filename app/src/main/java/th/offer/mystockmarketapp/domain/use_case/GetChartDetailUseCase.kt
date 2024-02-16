package th.offer.mystockmarketapp.domain.use_case

import th.offer.mystockmarketapp.domain.model.StockChartDetailModel
import th.offer.mystockmarketapp.domain.repository.StockChartDetailRepositoryInterface

class GetChartDetailUseCase(
    private val stockChartDetailRepositoryInterface: StockChartDetailRepositoryInterface
) {
    suspend fun execute(id: String, stockId: Int): StockChartDetailModel {
        return stockChartDetailRepositoryInterface.getChartDetail(
            id = id,
            stockId = stockId
        )
    }
}