package th.offer.mystockmarketapp.domain.repository

import th.offer.mystockmarketapp.domain.model.JittaRankingModel

interface RankingRepositoryInterface {
    suspend fun getRankingData(
        market: String,
        sectors: List<String>?,
        page: Int?,
        limit: Int?
    ): JittaRankingModel
}