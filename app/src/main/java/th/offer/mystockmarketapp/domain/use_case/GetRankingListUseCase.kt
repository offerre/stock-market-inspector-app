package th.offer.mystockmarketapp.domain.use_case

import th.offer.mystockmarketapp.domain.model.RankingDetailModel
import th.offer.mystockmarketapp.domain.repository.RankingRepositoryInterface

class GetRankingListUseCase(
    private val rankingRepositoryInterface: RankingRepositoryInterface,
) {
    suspend fun execute(
        market: String,
        sectors: List<String>?,
        page: Int?,
        limit: Int?
    ): List<RankingDetailModel>? {
        return rankingRepositoryInterface.getRankingData(
            market = market,
            sectors = sectors,
            page = page,
            limit = limit,
        ).data
    }
}