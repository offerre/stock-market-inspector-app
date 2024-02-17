package th.offer.mystockmarketapp.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import th.offer.StockSummaryQuery
import th.offer.mystockmarketapp.data.mapper.toModel
import th.offer.mystockmarketapp.domain.model.StockSummaryModel
import th.offer.mystockmarketapp.domain.repository.StockSummaryRepositoryImpl

class StockSummaryRepository(
    private val apolloClient: ApolloClient
) : StockSummaryRepositoryImpl {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getStockSummary(id: String, stockId: Int): StockSummaryModel {
        return apolloClient.query(
            StockSummaryQuery(
                id = Optional.present(id),
                stockId = Optional.present(stockId)
            )
        ).execute()
            .data
            ?.toModel() ?: StockSummaryModel(null)
    }
}