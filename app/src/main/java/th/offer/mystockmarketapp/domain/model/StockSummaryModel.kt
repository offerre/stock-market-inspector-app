package th.offer.mystockmarketapp.domain.model

import com.google.gson.annotations.SerializedName

data class StockSummaryModel(
    val stock: StockModel?
)

data class StockModel(
    val id: String?,
    val stockId: Int,
    val title: String?,
    val summary: String?,
    val sector: SectorModel?,
    val industry: String?,
    @SerializedName("currency_sign") val currencySign: String?,
    @SerializedName("last_complete_statement_key") val lastCompleteStatementKey: String?,
    @SerializedName("loss_chance") val lossChance: LossChange?,
    val price: Price?,
    val comparison: Comparison?,
    val updatedFinancialComplete: String?,
    val company: Company?,
    val jitta: StockChartDetailModel?
)

data class LossChange(
    val last: Double?,
    val updatedAt: String?
)

data class Price(
    val latest: Latest?
)

data class Latest(
    val close: Double?,
    val datetime: String?
)

data class Comparison(
    val market: Market?
)

data class Market(
    val rank: Int?,
    val member: Int?
)

data class Company(
    val link: List<Link>?
)

data class Link(
    val url: String?
)