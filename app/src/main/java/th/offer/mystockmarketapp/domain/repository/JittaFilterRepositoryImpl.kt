package th.offer.mystockmarketapp.domain.repository

import th.offer.mystockmarketapp.domain.model.JittaFilterDataModel

interface JittaFilterRepositoryImpl {
    suspend fun getJittaFilterParams(): JittaFilterDataModel
}