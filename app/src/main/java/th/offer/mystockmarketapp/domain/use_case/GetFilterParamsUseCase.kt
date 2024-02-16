package th.offer.mystockmarketapp.domain.use_case

import th.offer.mystockmarketapp.domain.model.JittaFilterDataModel
import th.offer.mystockmarketapp.domain.repository.JittaFilterRepositoryInterface

class GetFilterParamsUseCase(
    private val jittaFilterRepositoryInterface: JittaFilterRepositoryInterface
) {
    suspend fun execute(): JittaFilterDataModel {
        return jittaFilterRepositoryInterface.getJittaFilterParams()
    }
}