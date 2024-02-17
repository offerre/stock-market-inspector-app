package th.offer.mystockmarketapp.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import th.offer.StockChartDetailQuery
import th.offer.mystockmarketapp.data.mapper.toStockChartDetailModel
import th.offer.mystockmarketapp.domain.model.StockChartDetailModel
import th.offer.mystockmarketapp.domain.repository.StockChartDetailRepositoryImpl

class StockChartDetailRepository(
    private val apolloClient: ApolloClient
) : StockChartDetailRepositoryImpl {
    override suspend fun getChartDetail(id: String, stockId: Int): StockChartDetailModel {
        return apolloClient.query(
            StockChartDetailQuery(
                id = Optional.present(id),
                stockId = Optional.present(stockId),
            )
        ).execute()
            .data
            ?.stock
            ?.toStockChartDetailModel()
            ?: StockChartDetailModel(
                null, null, null,
                null, null, null
            )
    }
}