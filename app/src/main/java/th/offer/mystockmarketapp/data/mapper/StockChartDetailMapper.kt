package th.offer.mystockmarketapp.data.mapper

import th.offer.StockChartDetailQuery
import th.offer.mystockmarketapp.domain.model.ColumnDataModel
import th.offer.mystockmarketapp.domain.model.DisplayModel
import th.offer.mystockmarketapp.domain.model.FactorModel
import th.offer.mystockmarketapp.domain.model.LineInfoModel
import th.offer.mystockmarketapp.domain.model.LineModel
import th.offer.mystockmarketapp.domain.model.MonthlyPriceInfoModel
import th.offer.mystockmarketapp.domain.model.MonthlyPriceModel
import th.offer.mystockmarketapp.domain.model.PriceDiffInfoModel
import th.offer.mystockmarketapp.domain.model.PriceDiffModel
import th.offer.mystockmarketapp.domain.model.ScoreInfoModel
import th.offer.mystockmarketapp.domain.model.ScoreModel
import th.offer.mystockmarketapp.domain.model.SignInfoModel
import th.offer.mystockmarketapp.domain.model.SignModel
import th.offer.mystockmarketapp.domain.model.StockChartDetailModel
import th.offer.mystockmarketapp.domain.model.ValueInfoModel
import th.offer.mystockmarketapp.domain.model.ValueModel

fun StockChartDetailQuery.Stock?.toStockChartDetailModel(): StockChartDetailModel {
    return StockChartDetailModel(
        priceDiff = priceDiffModelMapper(this?.jitta?.priceDiff),
        monthlyPrice = monthlyPriceModelMapper(this?.jitta?.monthlyPrice),
        score = scoreModelMapper(this?.jitta?.score),
        line = lineModelMapper(this?.jitta?.line),
        factor = FactorModel(last = valueModelMapper(this?.jitta?.factor?.last?.value)),
        sign = SignModel(last = signInfoModelMapper(this?.jitta?.sign?.last))
    )
}

private fun priceDiffModelMapper(input: StockChartDetailQuery.PriceDiff?): PriceDiffModel {
    return PriceDiffModel(
        last = PriceDiffInfoModel(
            id = input?.last?.id,
            value = input?.last?.value,
            percent = input?.last?.onPriceDiffItem?.percent,
            type = input?.last?.onPriceDiffItem?.type,
        ),
        values = input?.values?.map {
            PriceDiffInfoModel(
                id = it?.id,
                value = it?.value,
                percent = it?.onPriceDiffItem?.percent,
                type = it?.onPriceDiffItem?.percent,
            )
        }?.toList(),
    )
}

private fun monthlyPriceModelMapper(input: StockChartDetailQuery.MonthlyPrice?): MonthlyPriceModel {
    return MonthlyPriceModel(
        total = input?.total,
        last = MonthlyPriceInfoModel(
            id = input?.last?.id,
            value = input?.last?.value,
            month = null, // Always null from GQL
            year = null // Always null from GQL
        ),
        values = input?.values?.map {
            MonthlyPriceInfoModel(
                id = it?.id,
                value = it?.value,
                month = it?.month,
                year = it?.year
            )
        }?.toList()
    )
}

private fun scoreModelMapper(input: StockChartDetailQuery.Score?): ScoreModel {
    return ScoreModel(
        last = ScoreInfoModel(
            id = input?.last?.id,
            value = input?.last?.value,
            quarter = null, // Always null from GQL
            year = null // Always null from GQL
        ),
        values = input?.values?.map {
            ScoreInfoModel(
                id = it?.id,
                value = it?.value,
                quarter = it?.quarter,
                year = it?.year,
            )
        }?.toList()
    )
}

private fun lineModelMapper(input: StockChartDetailQuery.Line?): LineModel {
    return LineModel(
        total = input?.total,
        values = input?.values?.map {
            LineInfoModel(
                id = it?.id,
                value = it?.value,
                month = it?.month,
                year = it?.year,
            )
        }?.toList()
    )
}

private fun valueModelMapper(input: StockChartDetailQuery.Value4?): ValueModel {

    return ValueModel(
        growth = ValueInfoModel(
            name = input?.growth?.name,
            value = input?.growth?.value,
            level = input?.growth?.level.toString()
        ),
        recent = ValueInfoModel(
            name = input?.recent?.name,
            value = input?.recent?.value,
            level = input?.recent?.level.toString()
        ),
        financial = ValueInfoModel(
            name = input?.financial?.name,
            value = input?.financial?.value,
            level = input?.financial?.level.toString()
        ),
        roi = ValueInfoModel(
            name = input?.`return`?.name,
            value = input?.`return`?.value,
            level = input?.`return`?.level.toString()
        ),
        management = ValueInfoModel(
            name = input?.management?.name,
            value = input?.management?.value,
            level = input?.management?.level.toString()
        ),
    )
}

private fun signInfoModelMapper(input: List<StockChartDetailQuery.Last4?>?): List<SignInfoModel>? {
    return input?.map {
        SignInfoModel(
            title = it?.title,
            type = it?.type,
            name = it?.name,
            value = it?.value,
            display = when (it?.display?.__typename) {
                "DisplayIPO" -> {
                    DisplayModel(
                        displayType = it.display.__typename,
                        title = it.display.onDisplayIPO?.title,
                        value = it.display.onDisplayIPO?.value,
                        column = null, // Always null
                        columnHead = null, // Always null
                        footer = null, // Always null
                    )
                }

                "DisplayTable" -> {
                    DisplayModel(
                        displayType = it.display.__typename,
                        title = it.display.onDisplayTable?.title,
                        columnHead = it.display.onDisplayTable?.columnHead?.map { text ->
                            text ?: ""
                        }?.toList(),
                        column = it.display.onDisplayTable?.columns?.map { columnData ->
                            ColumnDataModel(
                                name = columnData?.name,
                                data = columnData?.data?.map { data ->
                                    data ?: ""
                                }?.toList()
                            )
                        }?.toList(),
                        footer = it.display.onDisplayTable?.footer,
                        value = null, // Always null
                    )
                }

                else -> null
            }
        )
    }?.toList()
}