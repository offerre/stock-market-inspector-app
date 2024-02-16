package th.offer.mystockmarketapp.domain.repository

import th.offer.mystockmarketapp.domain.model.JittaFilterDataModel

interface JittaFilterRepositoryInterface {
    suspend fun getJittaFilterParams(): JittaFilterDataModel
}