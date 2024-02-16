package th.offer.mystockmarketapp.domain.use_case

import th.offer.mystockmarketapp.domain.repository.RankingRepositoryInterface

class GetTotalStockUseCase(
    private val rankingRepositoryInterface: RankingRepositoryInterface
) {
    suspend fun execute(
        market: String,
        sectors: List<String>?,
        page: Int?,
        limit: Int?
    ): Int {
        return rankingRepositoryInterface.getRankingData(
            market = market,
            sectors = sectors,
            page = page,
            limit = limit,
        ).count ?: 0
    }
}