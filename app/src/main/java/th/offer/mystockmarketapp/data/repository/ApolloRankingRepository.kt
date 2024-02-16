package th.offer.mystockmarketapp.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import th.offer.JittaRankingQuery
import th.offer.mystockmarketapp.data.mapper.toRankingData
import th.offer.mystockmarketapp.domain.model.JittaRankingModel
import th.offer.mystockmarketapp.domain.repository.RankingRepositoryInterface

class ApolloRankingRepository(
    private val apolloClient: ApolloClient,
) : RankingRepositoryInterface {

    override suspend fun getRankingData(
        market: String,
        sectors: List<String>?,
        page: Int?,
        limit: Int?
    ): JittaRankingModel {
        return apolloClient.query(
            JittaRankingQuery(
                market = market,
                sectors = Optional.presentIfNotNull(sectors),
                page = Optional.presentIfNotNull(page),
                limit = Optional.presentIfNotNull(limit)
            )
        ).execute()
            .data
            ?.jittaRanking
            ?.toRankingData() ?: JittaRankingModel(count = 0, data = emptyList())
    }
}