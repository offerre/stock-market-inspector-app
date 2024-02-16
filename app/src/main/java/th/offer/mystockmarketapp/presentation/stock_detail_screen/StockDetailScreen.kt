package th.offer.mystockmarketapp.presentation.stock_detail_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import th.offer.mystockmarketapp.R
import th.offer.mystockmarketapp.navigation.Screen
import th.offer.mystockmarketapp.presentation.common.Loading
import th.offer.mystockmarketapp.utils.roundOffDecimal

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StockDetailScreen(
    navController: NavController,
    viewModel: StockDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = {
            viewModel.fetchStockDetailData()
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar(state, navController) }
    ) { padding ->
        if (state.isLoading) {
            Loading()
        } else {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .pullRefresh(pullRefreshState)
            ) {
                LazyColumn {
                    item {
                        HeaderComponent(state)
                        SummaryComponent(state)
                    }
                }
                PullRefreshIndicator(
                    refreshing = state.isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    state: StockDetailState,
    navController: NavController,
) {
    val title = state.stockSummaryModel?.stock?.title

    CenterAlignedTopAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        // Top bar title
        title = {
            Text(text = title ?: stringResource(R.string.loading_text))
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.popBackStack(
                        route = Screen.RankingListScreen.route,
                        inclusive = false
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HeaderComponent(
    state: StockDetailState
) {
    Column {
        val score = (state.stockChartDetailModel?.score?.last?.value ?: 0.0).roundOffDecimal()
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(2f),
            ) {
                val title = state.stockSummaryModel?.stock?.title
                val exchangeId = state.stockSummaryModel?.stock?.id
                val rank = state.stockSummaryModel?.stock?.comparison?.market?.rank
                val totalMember = state.stockSummaryModel?.stock?.comparison?.market?.member
                // Title
                Text(
                    text = title.toString(),
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                    fontSize = 32.sp,
                    lineHeight = 36.sp
                )
                Spacer(modifier = Modifier.height(5.dp))
                // Exchange ID
                Text(
                    text = exchangeId.toString(),
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(5.dp))
                // Ranking
                Text(
                    text = "Jitta Ranking #$rank จาก $totalMember",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(MaterialTheme.colors.primary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Score
                Text(
                    text = score.toString(),
                    fontSize = 32.sp,
                    color = Color.White
                )
                // Jitta score text
                Text(
                    text = stringResource(R.string.jitta_score_text),
                    color = Color.White
                )
            }
        }
        // Current Price
        ListItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            text = {
                val latestPrice = state.stockSummaryModel?.stock?.price?.latest?.close
                val latestDate = state.stockSummaryModel?.stock?.price?.latest?.datetime
                Column {
                    Text(text = stringResource(R.string.latest_price_text, latestDate.toString()))
                    Text(
                        text = "฿ $latestPrice",
                        fontSize = 16.sp
                    )
                }
            }
        )
    }
}

@Composable
fun SummaryComponent(
    state: StockDetailState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Jitta Factors
        JittaFactorsCompose(state)
        // Jitta Signs
        Spacer(modifier = Modifier.height(10.dp))
        JittaSignsCompose(state)
        // Company info
        CompanyInfoCompose(state)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JittaFactorsCompose(
    state: StockDetailState
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val growth = state.stockChartDetailModel?.factor?.last?.growth
        val recent = state.stockChartDetailModel?.factor?.last?.recent
        val financial = state.stockChartDetailModel?.factor?.last?.financial
        val roi = state.stockChartDetailModel?.factor?.last?.roi
        val management = state.stockChartDetailModel?.factor?.last?.management
        val factorColor: (String) -> Int = { level ->
            when (level) {
                "HIGH" -> {
                    R.color.green
                }

                "MEDIUM" -> {
                    R.color.teal_200
                }

                "LOW" -> {
                    R.color.yellow
                }

                else -> {
                    R.color.red
                }
            }
        }
        // Jitta factors text
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(R.string.jitta_factors_title)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            // growth
            ListItem(
                text = { Text(text = "${growth?.name}") },
                trailing = { Text(text = "${growth?.value}") },
            )
            LinearProgressIndicator(
                progress = ((growth?.value ?: 0f).toFloat() / 100f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = colorResource(id = factorColor(growth?.level.toString())),
            )
            // recent
            ListItem(
                text = { Text(text = "${recent?.name}") },
                trailing = { Text(text = "${recent?.value}") },
            )
            LinearProgressIndicator(
                progress = ((recent?.value ?: 0f).toFloat() / 100f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = colorResource(id = factorColor(recent?.level.toString())),
            )
            // financial
            ListItem(
                text = { Text(text = "${financial?.name}") },
                trailing = { Text(text = "${financial?.value}") },
            )
            LinearProgressIndicator(
                progress = ((financial?.value ?: 0f).toFloat() / 100f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = colorResource(id = factorColor(financial?.level.toString())),
            )
            // return
            ListItem(
                text = { Text(text = "${roi?.name}") },
                trailing = { Text(text = "${roi?.value}") },
            )
            LinearProgressIndicator(
                progress = ((roi?.value ?: 0f).toFloat() / 100f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = colorResource(id = factorColor(roi?.level.toString())),
            )
            // management
            ListItem(
                text = { Text(text = "${management?.name}") },
                trailing = { Text(text = "${management?.value}") },
            )
            LinearProgressIndicator(
                progress = ((management?.value ?: 0f).toFloat() / 100f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = colorResource(id = factorColor(management?.level.toString())),
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JittaSignsCompose(
    state: StockDetailState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Jitta signs header
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(R.string.jiia_signs_header)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            val signsList = state.stockChartDetailModel?.sign?.last
            val iconColor: (String) -> Int = { type ->
                when (type) {
                    "good" -> {
                        R.color.green
                    }

                    else -> {
                        R.color.red
                    }
                }
            }
            var expandItemName by remember {
                mutableStateOf(emptyList<String>())
            }

            signsList?.forEach { item ->
                Column {
                    ListItem(
                        // Signs header
                        text = {
                            Text(
                                text = "${item.title}",
                                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                            )
                        },
                        trailing = {
                            Icon(
                                imageVector = if (expandItemName.contains(item.name)) {
                                    Icons.Default.KeyboardArrowUp
                                } else {
                                    Icons.Default.KeyboardArrowDown
                                },
                                contentDescription = null,
                            )
                        },
                        // Sign sub header
                        secondaryText = { Text(text = "${item.value}") },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .width(width = 2.dp)
                                    .height(40.dp)
                                    .background(
                                        color = colorResource(id = iconColor(item.type.toString())),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                            )
                        },
                        modifier = Modifier.clickable {
                            if (item.name != null) {
                                expandItemName = if (expandItemName.contains(item.name)) {
                                    expandItemName - item.name
                                } else {
                                    expandItemName + item.name
                                }
                            }
                        }
                    )
                    if (expandItemName.contains(item.name))
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            when (item.display?.displayType) {
                                "DisplayTable" -> {
                                    LazyRow {
                                        item {
                                            Row {
                                                Column(
                                                    modifier = Modifier
                                                        .background(
                                                            color = colorResource(
                                                                id = R.color.ocean
                                                            )
                                                        )
                                                        .padding(horizontal = 6.dp, vertical = 4.dp)
                                                ) {
                                                    Box {
                                                        Text(
                                                            text = "  ",
                                                            Modifier.padding(4.dp)
                                                        )
                                                    }
                                                    item.display.columnHead?.forEach { headItem ->
                                                        Box(
                                                            Modifier.align(alignment = Alignment.CenterHorizontally)
                                                        ) {
                                                            Text(
                                                                text = headItem,
                                                                Modifier.padding(4.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                                item.display.column?.forEach { columnHeadItem ->
                                                    Column(
                                                        horizontalAlignment = Alignment.End,
                                                        modifier = Modifier
                                                            .background(
                                                                color = colorResource(
                                                                    id = R.color.ocean_light
                                                                )
                                                            )
                                                            .padding(
                                                                horizontal = 6.dp,
                                                                vertical = 4.dp
                                                            )
                                                    ) {
                                                        Box(
                                                            Modifier
                                                                .align(alignment = Alignment.CenterHorizontally)
                                                                .padding(4.dp)

                                                        ) {
                                                            Text(text = "${columnHeadItem.name}")
                                                        }
                                                        columnHeadItem.data?.forEach { columnItem ->
                                                            Column(
                                                                Modifier
                                                                    .padding(4.dp)
                                                            ) {
                                                                Text(text = columnItem)
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                else -> {
                                    Column(Modifier.padding(6.dp)) {
                                        Text(
                                            text = item.display?.title ?: "",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    color = colorResource(
                                                        id = R.color.ocean
                                                    )
                                                )
                                                .padding(horizontal = 6.dp, vertical = 4.dp)
                                        )
                                        Text(
                                            text = item.display?.value.toString(),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    color = colorResource(
                                                        id = R.color.ocean_light
                                                    )
                                                )
                                                .padding(horizontal = 6.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                }
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompanyInfoCompose(
    state: StockDetailState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        val summary = state.stockSummaryModel?.stock?.summary
        val expanded = remember { mutableStateOf(false) }
        Column {
            // Company info header
            Text(text = stringResource(R.string.company_info_header))
            Spacer(modifier = Modifier.height(16.dp))
            // Company summary
            Text(
                text = summary.toString(),
                overflow = TextOverflow.Ellipsis,
                modifier = when (expanded.value) {
                    true -> {
                        Modifier.height(IntrinsicSize.Max)
                    }

                    false -> {
                        Modifier.height(100.dp)
                    }
                }
            )
            // More text
            if (!expanded.value)
                Text(
                    text = stringResource(R.string.more_text),
                    color = colorResource(id = R.color.teal_200),
                    modifier = Modifier
                        .clickable {
                            expanded.value = true
                        }
                )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column {

            // Company info title
            Text(text = stringResource(R.string.company_info_title))
            Column {
                val uriHandler = LocalUriHandler.current
                val url = state.stockSummaryModel?.stock?.company?.link?.first()?.url
                val sectorName = state.stockSummaryModel?.stock?.sector?.name
                val industryName = state.stockSummaryModel?.stock?.industry
                // Sector component
                ListItem(
                    // Sector title
                    text = { Text(text = stringResource(R.string.sector_title)) },
                    // Sector name
                    trailing = { Text(text = sectorName.toString()) },
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                // Industry component
                ListItem(
                    // Industry title
                    text = { Text(text = stringResource(R.string.industry_title)) },
                    // Industry name
                    trailing = { Text(text = industryName.toString()) },
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                // Link component
                ListItem(
                    // Industry title
                    text = { Text(text = stringResource(R.string.company_link_title)) },
                    // Industry name
                    trailing = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            tint = colorResource(id = R.color.teal_200)
                        )
                    },
                    modifier = Modifier.clickable {
                        if (url != null) {
                            uriHandler.openUri("https://${url}")
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}