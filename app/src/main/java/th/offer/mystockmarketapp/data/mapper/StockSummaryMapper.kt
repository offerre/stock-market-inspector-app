package th.offer.mystockmarketapp.data.mapper

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import th.offer.StockSummaryQuery
import th.offer.mystockmarketapp.domain.model.Company
import th.offer.mystockmarketapp.domain.model.Comparison
import th.offer.mystockmarketapp.domain.model.Latest
import th.offer.mystockmarketapp.domain.model.Link
import th.offer.mystockmarketapp.domain.model.LossChange
import th.offer.mystockmarketapp.domain.model.Market
import th.offer.mystockmarketapp.domain.model.Price
import th.offer.mystockmarketapp.domain.model.SectorModel
import th.offer.mystockmarketapp.domain.model.StockModel
import th.offer.mystockmarketapp.domain.model.StockSummaryModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun StockSummaryQuery.Data.toModel(): StockSummaryModel {
    return StockSummaryModel(
        stock = StockModel(
            id = this.stock?.id,
            stockId = this.stock?.stockId ?: 0,
            title = this.stock?.title,
            summary = this.stock?.summary,
            sector = SectorModel(
                id = this.stock?.sector?.id ?: "",
                name = this.stock?.sector?.name
            ),
            industry = this.stock?.industry,
            currencySign = this.stock?.currency_sign,
            lastCompleteStatementKey = this.stock?.last_complete_statement_key,
            lossChance = LossChange(
                last = this.stock?.loss_chance?.last,
                updatedAt = convertToDateTimeString(this.stock?.loss_chance?.updatedAt)
            ),
            price = Price(
                latest = Latest(
                    close = this.stock?.price?.latest?.close,
                    datetime = convertToDateTimeString(this.stock?.price?.latest?.datetime),
                )
            ),
            comparison = Comparison(
                market = Market(
                    rank = this.stock?.comparison?.market?.rank,
                    member = this.stock?.comparison?.market?.member
                )
            ),
            updatedFinancialComplete = convertToDateTimeString(this.stock?.updatedFinancialComplete),
            company = Company(
                link = this.stock?.company?.link?.map {
                    Link(it?.url)
                }?.toList()
            ),
            jitta = null
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun convertToDateTimeString(dt: Any?): String {
    Log.d("convertToDateTimeString", "dtString: $dt")
    if (dt != null) {
        val inputFormat =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        val outputFormat = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("th", "TH"))
        val date = LocalDateTime.parse(dt.toString(), inputFormat)
        Log.d("convertToDateTimeString", "output: ${date.format(outputFormat)}")
        return date.format(outputFormat)
    }
    return ""
}