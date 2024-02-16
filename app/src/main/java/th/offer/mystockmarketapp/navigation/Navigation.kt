package th.offer.mystockmarketapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import th.offer.mystockmarketapp.presentation.ranking_list_screen.RankingListScreen
import th.offer.mystockmarketapp.presentation.stock_detail_screen.StockDetailScreen

@Composable
fun Navigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.RankingListScreen.route
    ) {
        composable(route = Screen.RankingListScreen.route) {
            RankingListScreen(
                navigateToStockDetailScreen = { stockId, id ->
                    navController.navigate("${Screen.StockDetailScreen.route}/$id/$stockId")
                }
            )
        }
        composable(
            route = "${Screen.StockDetailScreen.route}/{$ID}/{$STOCK_ID}",
            arguments = listOf(
                navArgument(ID) { type = NavType.StringType },
                navArgument(STOCK_ID) { type = NavType.IntType }
            )
        ) {
            val id = it.arguments?.getString(ID)
            val stockId = it.arguments?.getInt(STOCK_ID)
            if (id != null && stockId != null) {
                StockDetailScreen(navController = navController)
            }
        }
    }
}