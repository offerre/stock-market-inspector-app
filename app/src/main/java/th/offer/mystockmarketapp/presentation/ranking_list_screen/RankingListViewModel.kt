package th.offer.mystockmarketapp.presentation.ranking_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import th.offer.mystockmarketapp.data.repository.RankingPagingSource
import th.offer.mystockmarketapp.domain.use_case.GetFilterParamsUseCase
import th.offer.mystockmarketapp.domain.use_case.GetTotalStockUseCase
import javax.inject.Inject


@HiltViewModel
class RankingListViewModel @Inject constructor(
    private val getFilterParamsUseCase: GetFilterParamsUseCase,
    private val getTotalStockUseCase: GetTotalStockUseCase,
    private val rankingPagingSource: RankingPagingSource,
) : ViewModel() {

    private val _state = MutableStateFlow(
        RankingListState(
            selectedCountryName = "",
            selectedCategoryName = "",
        )
    )
    val state = _state.asStateFlow()

    var pager = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            RankingPagingSource(
                apolloClient = rankingPagingSource.getApollo,
                market = _state.value.selectedCountryCode,
                sectors = _state.value.selectedCategory,
            )
        }
    ).flow.cachedIn(viewModelScope)

    init {
        fetchTotalListCount()
        fetchFilterParams()
    }

    fun getCountryName(code: String): String {
        return if (_state.value.filterParams.availableCountry.isNotEmpty()) {
            val countryName = _state.value.filterParams.availableCountry
                .first { it.code == code }.name
            countryName.toString()
        } else {
            ""
        }
    }

    fun getCategoryName(id: String?): String? {
        return if (_state.value.filterParams.listJittaSectorType.isNotEmpty() && id != null) {
            val categoryName = _state.value.filterParams.listJittaSectorType
                .first { it.id == id }.name
            categoryName
        } else {
            null
        }
    }

    fun fetchTotalListCount(
    ) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            _state.update {
                it.copy(
                    total = getTotalStockUseCase.execute(
                        market = _state.value.selectedCountryCode,
                        sectors = _state.value.selectedCategory,
                        page = 0,
                        limit = 30
                    ),
                    isLoading = false
                )
            }
        }
    }

    private fun fetchFilterParams() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            _state.update {
                it.copy(
                    filterParams = getFilterParamsUseCase.execute(),
                )
            }
            _state.update {
                it.copy(
                    isLoading = false,
                    selectedCountryName = getCountryName(it.selectedCountryCode),
                    selectedCategoryName = getCategoryName(it.selectedCategory?.first())
                )
            }
        }
    }
}