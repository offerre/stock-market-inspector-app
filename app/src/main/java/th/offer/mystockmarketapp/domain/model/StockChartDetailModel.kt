package th.offer.mystockmarketapp.domain.model

import com.google.gson.annotations.SerializedName

data class StockChartDetailModel(
    val priceDiff: PriceDiffModel?,
    val monthlyPrice: MonthlyPriceModel?,
    val score: ScoreModel?,
    val line: LineModel?,
    val factor: FactorModel?,
    val sign: SignModel?
)

data class PriceDiffModel(
    val last: PriceDiffInfoModel?,
    val values: List<PriceDiffInfoModel>?,
)

data class PriceDiffInfoModel(
    val id: String?,
    val value: Double?,
    val type: String?,
    val percent: String?
)

data class MonthlyPriceModel(
    val total: Int?,
    val last: MonthlyPriceInfoModel?,
    val values: List<MonthlyPriceInfoModel>?
)

data class MonthlyPriceInfoModel(
    val id: String?,
    val value: Double?,
    val year: Int?,
    val month: Int?
)

data class ScoreModel(
    val last: ScoreInfoModel?,
    val values: List<ScoreInfoModel>?
)

data class ScoreInfoModel(
    val id: String?,
    val value: Double?,
    val quarter: Int?,
    val year: Int?
)

data class LineModel(
    val total: Int?,
    val values: List<LineInfoModel>?,
)

data class LineInfoModel(
    val id: String?,
    val value: Double?,
    val year: Int?,
    val month: Int?
)

data class FactorModel(
    val last: ValueModel?,
)

data class ValueModel(
    val growth: ValueInfoModel?,
    val recent: ValueInfoModel?,
    val financial: ValueInfoModel?,
    @SerializedName("return") val roi: ValueInfoModel?,
    val management: ValueInfoModel?,
)

data class ValueInfoModel(
    val value: Double?,
    val name: String?,
    val level: String?
)

data class SignModel(
    val last: List<SignInfoModel>?
)

data class SignInfoModel(
    val title: String?,
    val type: String?,
    val name: String?,
    val value: String?,
    val display: DisplayModel?
)

data class DisplayModel(
    @SerializedName("__typename") val displayType: String?,
    val title: String?,
    val value: Int?,
    val columnHead: List<String>?,
    val column: List<ColumnDataModel>?,
    val footer: String?
)

data class ColumnDataModel(
    val name: String?,
    val data: List<String>?
)