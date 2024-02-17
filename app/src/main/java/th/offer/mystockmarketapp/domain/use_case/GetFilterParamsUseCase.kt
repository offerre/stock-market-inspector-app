package th.offer.mystockmarketapp.domain.use_case

import th.offer.mystockmarketapp.domain.model.JittaFilterDataModel
import th.offer.mystockmarketapp.domain.repository.JittaFilterRepositoryImpl

class GetFilterParamsUseCase(
    private val jittaFilterRepositoryImpl: JittaFilterRepositoryImpl
) {
    suspend fun execute(): JittaFilterDataModel {
        return jittaFilterRepositoryImpl.getJittaFilterParams()
    }
}