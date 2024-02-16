package th.offer.mystockmarketapp.data.mapper

import th.offer.JittaFilterDataQuery
import th.offer.mystockmarketapp.domain.model.CountryModel
import th.offer.mystockmarketapp.domain.model.JittaFilterDataModel
import th.offer.mystockmarketapp.domain.model.JittaSectorTypeModel

fun JittaFilterDataQuery.Data.mapper(): JittaFilterDataModel {
    return JittaFilterDataModel(
        listJittaSectorType =
        // this first list act being like value of all sector
        listOf(
            JittaSectorTypeModel(
                id = "",
                name = null
            )
        ).plus(
            this.listJittaSectorType?.map { item ->
                JittaSectorTypeModel(
                    id = item?.id ?: "",
                    name = item?.name
                )
            }?.toList() ?: emptyList()
        ),
        availableCountry =
        this.availableCountry?.map { item ->
            CountryModel(
                code = item?.code,
                name = item?.name
            )
        }?.toList() ?: emptyList()
    )
}
