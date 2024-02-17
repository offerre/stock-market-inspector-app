package th.offer.mystockmarketapp.domain.use_case

import th.offer.mystockmarketapp.domain.repository.RankingRepositoryImpl

class GetTotalStockUseCase(
    private val rankingRepositoryImpl: RankingRepositoryImpl
) {
    suspend fun execute(
        market: String,
        sectors: List<String>?,
        page: Int?,
        limit: Int?
    ): Int {
        return rankingRepositoryImpl.getRankingData(
            market = market,
            sectors = sectors,
            page = page,
            limit = limit,
        ).count ?: 0
    }
}