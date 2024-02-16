package th.offer.mystockmarketapp.data.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import th.offer.mystockmarketapp.data.repository.ApolloRankingRepository
import th.offer.mystockmarketapp.data.repository.JittaFilterRepository
import th.offer.mystockmarketapp.data.repository.RankingPagingSource
import th.offer.mystockmarketapp.data.repository.StockChartDetailRepository
import th.offer.mystockmarketapp.data.repository.StockSummaryRepository
import th.offer.mystockmarketapp.domain.repository.JittaFilterRepositoryInterface
import th.offer.mystockmarketapp.domain.repository.RankingRepositoryInterface
import th.offer.mystockmarketapp.domain.repository.StockChartDetailRepositoryInterface
import th.offer.mystockmarketapp.domain.repository.StockSummaryRepositoryInterface
import th.offer.mystockmarketapp.domain.use_case.GetChartDetailUseCase
import th.offer.mystockmarketapp.domain.use_case.GetFilterParamsUseCase
import th.offer.mystockmarketapp.domain.use_case.GetRankingListUseCase
import th.offer.mystockmarketapp.domain.use_case.GetStockSummaryUseCase
import th.offer.mystockmarketapp.domain.use_case.GetTotalStockUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://current--tj-stock-rankin-dev.apollographos.net/graphql")
            .addHttpHeader("Content-Language", "th")
            .addHttpInterceptor(LoggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRankingRepositoryInterface(apolloClient: ApolloClient): RankingRepositoryInterface {
        return ApolloRankingRepository(apolloClient = apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetRankingDataUseCase(rankingRepositoryInterface: RankingRepositoryInterface): GetRankingListUseCase {
        return GetRankingListUseCase(rankingRepositoryInterface = rankingRepositoryInterface)
    }

    @Provides
    @Singleton
    fun provideGetTotalStockUseCase(rankingRepositoryInterface: RankingRepositoryInterface): GetTotalStockUseCase {
        return GetTotalStockUseCase(rankingRepositoryInterface = rankingRepositoryInterface)
    }

    @Provides
    @Singleton
    fun provideRankingPagingSource(apolloClient: ApolloClient): RankingPagingSource {
        return RankingPagingSource(apolloClient = apolloClient)
    }

    @Provides
    @Singleton
    fun provideJittaFilterRepositoryInterface(apolloClient: ApolloClient): JittaFilterRepositoryInterface {
        return JittaFilterRepository(apolloClient = apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetFilterParamsUseCase(jittaFilterRepositoryInterface: JittaFilterRepositoryInterface): GetFilterParamsUseCase {
        return GetFilterParamsUseCase(jittaFilterRepositoryInterface = jittaFilterRepositoryInterface)
    }

    @Provides
    @Singleton
    fun provideStockSummaryRepositoryInterface(apolloClient: ApolloClient): StockSummaryRepositoryInterface {
        return StockSummaryRepository(apolloClient = apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetStockSummaryUseCase(stockSummaryRepositoryInterface: StockSummaryRepositoryInterface): GetStockSummaryUseCase {
        return GetStockSummaryUseCase(stockSummaryRepositoryInterface = stockSummaryRepositoryInterface)
    }

    @Provides
    @Singleton
    fun provideStockChartDetailRepositoryInterface(apolloClient: ApolloClient): StockChartDetailRepositoryInterface {
        return StockChartDetailRepository(apolloClient = apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetChartDetailUseCase(stockChartDetailRepositoryInterface: StockChartDetailRepositoryInterface): GetChartDetailUseCase {
        return GetChartDetailUseCase(stockChartDetailRepositoryInterface = stockChartDetailRepositoryInterface)
    }
}
