package th.offer.mystockmarketapp.navigation

sealed class Screen(
    val route: String,
) {
    data object RankingListScreen : Screen("ranking_list_screen")
    data object StockDetailScreen : Screen("stock_detail_screen")
}

const val ID = "id"
const val STOCK_ID = "stockId"