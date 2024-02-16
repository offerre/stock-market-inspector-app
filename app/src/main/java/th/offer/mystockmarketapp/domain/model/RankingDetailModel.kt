package th.offer.mystockmarketapp.domain.model

data class RankingDetailModel(
    val id: String?,
    val stockId: Int,
    val rank: Int?,
    val title: String?,
    val jittaScore: Double?,
    val nativeName: String?,
    val sector: SectorModel?,
    val industry: String?,
)
