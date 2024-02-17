package th.offer.mystockmarketapp.data.repository

import com.apollographql.apollo3.ApolloClient
import th.offer.JittaFilterDataQuery
import th.offer.mystockmarketapp.data.mapper.mapper
import th.offer.mystockmarketapp.domain.model.JittaFilterDataModel
import th.offer.mystockmarketapp.domain.repository.JittaFilterRepositoryImpl

class JittaFilterRepository(
    private val apolloClient: ApolloClient
) : JittaFilterRepositoryImpl {
    override suspend fun getJittaFilterParams(): JittaFilterDataModel {
        return apolloClient.query(
            JittaFilterDataQuery()
        ).execute()
            .data
            ?.mapper() ?: JittaFilterDataModel(
                listJittaSectorType = emptyList(),
                availableCountry = emptyList()
            )
    }
}