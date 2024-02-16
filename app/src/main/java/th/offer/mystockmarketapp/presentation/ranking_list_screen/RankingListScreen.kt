package th.offer.mystockmarketapp.presentation.ranking_list_screen

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.launch
import th.offer.mystockmarketapp.R
import th.offer.mystockmarketapp.domain.model.RankingDetailModel
import th.offer.mystockmarketapp.presentation.common.Loading
import th.offer.mystockmarketapp.presentation.ranking_list_screen.compose.SelectionDialogCompose

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RankingListScreen(
    viewModel: RankingListViewModel = hiltViewModel(),
    navigateToStockDetailScreen: (stockId: Int, id: String?) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val lazyPagingItems = viewModel.pager.collectAsLazyPagingItems()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = lazyPagingItems.loadState.refresh is LoadState.Loading,
        onRefresh = {
            viewModel.fetchTotalListCount()
            lazyPagingItems.refresh()
            coroutineScope.launch {
                // reset scroll position
                lazyListState.scrollToItem(0)
            }
        },
        refreshThreshold = 100.dp
    )

    val countryDialog = remember { mutableStateOf(false) }
    val categoryDialog = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarCompose(
                state = state,
                lazyListState = lazyListState,
                countryDialog = countryDialog,
                categoryDialog = categoryDialog
            )
        }
    ) {
        if (lazyPagingItems.loadState.refresh is LoadState.Loading
            || state.isLoading
        ) {
            Loading()
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                RankingSectionCompose(
                    state = state,
                    lazyListState = lazyListState,
                    lazyPagingItems = lazyPagingItems,
                    onItemClick = navigateToStockDetailScreen
                )
                PullRefreshIndicator(
                    refreshing = lazyPagingItems.loadState.refresh is LoadState.Loading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )

                // Country filter dialog
                if (countryDialog.value) {
                    SelectionDialogCompose(
                        listItem = state.filterParams.availableCountry,
                        onClick = {
                            state.selectedCountryCode = it
                            state.selectedCountryName = viewModel.getCountryName(it)
                            // reset category filter once country filter changed
                            state.selectedCategory = null
                            state.selectedCategoryName = null

                            countryDialog.value = false
                            viewModel.fetchTotalListCount()
                            lazyPagingItems.refresh()
                            coroutineScope.launch {
                                // reset scroll position
                                lazyListState.scrollToItem(0)
                            }
                        },
                        onDismissRequest = {
                            countryDialog.value = false
                        }
                    )
                }

                // Category filter dialog
                if (categoryDialog.value) {
                    SelectionDialogCompose(
                        listItem = state.filterParams.listJittaSectorType,
                        onClick = {
                            state.selectedCategory = if (it.isNotEmpty()) listOf(it) else null
                            state.selectedCategoryName = viewModel.getCategoryName(it)
                            categoryDialog.value = false

                            viewModel.fetchTotalListCount()
                            lazyPagingItems.refresh()
                            coroutineScope.launch {
                                // reset scroll position
                                lazyListState.scrollToItem(0)
                            }
                        },
                        onDismissRequest = {
                            categoryDialog.value = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RankingSectionCompose(
    state: RankingListState,
    lazyListState: LazyListState,
    lazyPagingItems: LazyPagingItems<RankingDetailModel>,
    onItemClick: (stockId: Int, id: String?) -> Unit,
) {
    val padding by animateDpAsState(
        targetValue = if (lazyListState.isScrolled) TOP_BAR_HEIGHT else (TOP_BAR_HEIGHT * 2) + DIVIDER_GAP,
        animationSpec = tween(durationMillis = 300),
        label = "rankingContentPaddingAnimate"
    )
    LazyColumn(
        modifier = Modifier.padding(top = padding),
        state = lazyListState
    ) {
        items(count = lazyPagingItems.itemCount) { index ->
            lazyPagingItems[index]?.let { rankingDetail ->
                StockListItem(
                    total = state.total,
                    rankingDetailModel = rankingDetail,
                    onItemClick = onItemClick
                )
            }
        }

        if (lazyPagingItems.loadState.refresh == LoadState.Loading ||
            lazyPagingItems.loadState.append == LoadState.Loading
        ) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
private fun StockListItem(
    total: Int,
    rankingDetailModel: RankingDetailModel,
    onItemClick: (stockId: Int, id: String?) -> Unit
) {
    Column {
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .clickable {
                    onItemClick(rankingDetailModel.stockId, rankingDetailModel.id)
                },
            headlineContent = {
                Text(
                    text = "${rankingDetailModel.title}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            },
            supportingContent = {
                Text(
                    text = "${rankingDetailModel.id}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            },
            trailingContent = {
                Row {
                    Text(
                        text = rankingDetailModel.rank.toString(),
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = " / $total",
                        color = Color.Gray,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                }
            }
        )
        Divider(modifier = Modifier.padding(horizontal = 16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarCompose(
    state: RankingListState,
    lazyListState: LazyListState,
    countryDialog: MutableState<Boolean>,
    categoryDialog: MutableState<Boolean>
) {
    Column {
        CenterAlignedTopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.primary)
                .animateContentSize(animationSpec = tween(durationMillis = 300))
                .height(height = if (lazyListState.isScrolled) 0.dp else TOP_BAR_HEIGHT),
            title = {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.purple_500)
            )
        )
        FilterSectionCompose(
            state = state,
            countryDialog = countryDialog,
            categoryDialog = categoryDialog
        )
    }
}

@Composable
fun FilterSectionCompose(
    state: RankingListState,
    countryDialog: MutableState<Boolean>,
    categoryDialog: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
            .height(TOP_BAR_HEIGHT + 10.dp)
            .fillMaxWidth()
            .padding(vertical = 0.dp)
            .background(MaterialTheme.colors.surface),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Country filter button
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    countryDialog.value = true
                }) {
                Text(
                    modifier = Modifier
                        .weight(4f)
                        .wrapContentWidth(),
                    text = state.selectedCountryName ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                )
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            // Category filter button
            OutlinedButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    categoryDialog.value = true
                }) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(4f)
                            .wrapContentWidth(),
                        text = state.selectedCategoryName
                            ?: stringResource(id = R.string.all_category_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                    )
                    Icon(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }
        }
        Divider()
    }
}

// Reference from Material3 docs https://m3.material.io/components/top-app-bar/specs#dc0c1708-3ac6-4ed3-8746-67cb248f0937
val TOP_BAR_HEIGHT = 64.dp
val DIVIDER_GAP = 20.dp
val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0
