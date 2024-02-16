package th.offer.mystockmarketapp.presentation.ranking_list_screen

import th.offer.mystockmarketapp.domain.model.JittaFilterDataModel
import th.offer.mystockmarketapp.domain.model.RankingDetailModel

data class RankingListState(
    var isLoading: Boolean = false,
    var selectedCountryCode: String = "TH",
    var selectedCategory: List<String>? = null,
    var selectedCountryName: String?,
    var selectedCategoryName: String?,
    var total: Int = 0,
    var rankingList: List<RankingDetailModel>? = null,
    val filterParams: JittaFilterDataModel = JittaFilterDataModel(emptyList(), emptyList())
)