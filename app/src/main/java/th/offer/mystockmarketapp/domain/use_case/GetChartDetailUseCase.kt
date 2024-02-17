package th.offer.mystockmarketapp.domain.use_case

import th.offer.mystockmarketapp.domain.model.StockChartDetailModel
import th.offer.mystockmarketapp.domain.repository.StockChartDetailRepositoryImpl

class GetChartDetailUseCase(
    private val stockChartDetailRepositoryImpl: StockChartDetailRepositoryImpl
) {
    suspend fun execute(id: String, stockId: Int): StockChartDetailModel {
        return stockChartDetailRepositoryImpl.getChartDetail(
            id = id,
            stockId = stockId
        )
    }
}