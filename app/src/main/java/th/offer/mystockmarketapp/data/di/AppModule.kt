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
import th.offer.mystockmarketapp.domain.repository.JittaFilterRepositoryImpl
import th.offer.mystockmarketapp.domain.repository.RankingRepositoryImpl
import th.offer.mystockmarketapp.domain.repository.StockChartDetailRepositoryImpl
import th.offer.mystockmarketapp.domain.repository.StockSummaryRepositoryImpl
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
    fun provideRankingRepositoryInterface(apolloClient: ApolloClient): RankingRepositoryImpl {
        return ApolloRankingRepository(apolloClient = apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetRankingDataUseCase(rankingRepositoryImpl: RankingRepositoryImpl): GetRankingListUseCase {
        return GetRankingListUseCase(rankingRepositoryImpl = rankingRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideGetTotalStockUseCase(rankingRepositoryImpl: RankingRepositoryImpl): GetTotalStockUseCase {
        return GetTotalStockUseCase(rankingRepositoryImpl = rankingRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideRankingPagingSource(apolloClient: ApolloClient): RankingPagingSource {
        return RankingPagingSource(apolloClient = apolloClient)
    }

    @Provides
    @Singleton
    fun provideJittaFilterRepositoryInterface(apolloClient: ApolloClient): JittaFilterRepositoryImpl {
        return JittaFilterRepository(apolloClient = apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetFilterParamsUseCase(jittaFilterRepositoryImpl: JittaFilterRepositoryImpl): GetFilterParamsUseCase {
        return GetFilterParamsUseCase(jittaFilterRepositoryImpl = jittaFilterRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideStockSummaryRepositoryInterface(apolloClient: ApolloClient): StockSummaryRepositoryImpl {
        return StockSummaryRepository(apolloClient = apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetStockSummaryUseCase(stockSummaryRepositoryImpl: StockSummaryRepositoryImpl): GetStockSummaryUseCase {
        return GetStockSummaryUseCase(stockSummaryRepositoryImpl = stockSummaryRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideStockChartDetailRepositoryInterface(apolloClient: ApolloClient): StockChartDetailRepositoryImpl {
        return StockChartDetailRepository(apolloClient = apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetChartDetailUseCase(stockChartDetailRepositoryImpl: StockChartDetailRepositoryImpl): GetChartDetailUseCase {
        return GetChartDetailUseCase(stockChartDetailRepositoryImpl = stockChartDetailRepositoryImpl)
    }
}
