package th.offer.mystockmarketapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import th.offer.JittaRankingQuery
import th.offer.mystockmarketapp.data.mapper.toRankingData
import th.offer.mystockmarketapp.domain.model.JittaRankingModel
import th.offer.mystockmarketapp.domain.model.RankingDetailModel
import java.io.IOException
import java.net.HttpRetryException

class RankingPagingSource(
    private val apolloClient: ApolloClient,
    private val market: String = "th",
    private val sectors: List<String>? = null,
) : PagingSource<Int, RankingDetailModel>() {

    val getApollo = apolloClient

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RankingDetailModel> {
        try {
            // Key may be null during a refresh, if no explicit key is passed into Pager
            // construction. Use 0 as default, because our API is indexed started at index 0
            val pageNumber = params.key ?: 0
            val response =
                apolloClient.query(
                    JittaRankingQuery(
                        market = market,
                        sectors = Optional.presentIfNotNull(sectors),
                        page = Optional.present(pageNumber),
                        limit = Optional.present(30)
                    )
                )
                    .execute()
                    .data
                    ?.jittaRanking
                    ?.toRankingData() ?: JittaRankingModel(count = 0, data = emptyList())

            // Since 0 is the lowest page number, return null to signify no more pages should
            // be loaded before it.
//            val prevKey = if (pageNumber > 0) pageNumber - 1 else null
            // Only paging forward.
            val prevKey = null

            // This API defines that it's out of data when a page returns empty. When out of
            // data, we return `null` to signify no more pages should be loaded
            val nextKey = if (response.data?.isNotEmpty() == true) pageNumber + 1 else null

            return LoadResult.Page(
                data = response.data ?: emptyList(),
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpRetryException) {
            // HttpRetryException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RankingDetailModel>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; need to handle nullability here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        /*return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }*/
        return null
    }

}