package th.offer.mystockmarketapp.data.mapper

import th.offer.JittaRankingQuery
import th.offer.mystockmarketapp.domain.model.JittaRankingModel
import th.offer.mystockmarketapp.domain.model.RankingDetailModel
import th.offer.mystockmarketapp.domain.model.SectorModel

fun JittaRankingQuery.JittaRanking.toRankingData(): JittaRankingModel {
    return JittaRankingModel(
        count = this.count ?: 0,
        data = this.data?.mapNotNull { result ->
            RankingDetailModel(
                id = result?.id,
                stockId = result?.stockId ?: -1,
                rank = result?.rank,
                title = result?.title,
                jittaScore = result?.jittaScore,
                nativeName = result?.nativeName,
                sector = SectorModel(
                    id = result?.sector?.id.orEmpty(),
                    name = result?.sector?.name
                ),
                industry = result?.industry,
            )
        }.orEmpty()
    )
}