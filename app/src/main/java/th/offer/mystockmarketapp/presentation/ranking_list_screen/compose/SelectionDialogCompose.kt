package th.offer.mystockmarketapp.presentation.ranking_list_screen.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import th.offer.mystockmarketapp.R
import th.offer.mystockmarketapp.domain.model.CountryModel
import th.offer.mystockmarketapp.domain.model.JittaSectorTypeModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> SelectionDialogCompose(
    listItem: List<T>,
    onClick: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        LazyColumn {
            items(items = listItem) {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .clickable {
                            when (it) {
                                is JittaSectorTypeModel -> {
                                    onClick(it.id)
                                }

                                is CountryModel -> {
                                    onClick(it.code.toString())
                                }
                            }
                        },
                    text = {
                        Text(
                            text = when (it) {
                                is JittaSectorTypeModel -> {
                                    it.name ?: stringResource(id = R.string.all_category_title)
                                }

                                is CountryModel -> {
                                    it.name.toString()
                                }

                                else -> "null"
                            },
                            textAlign = TextAlign.Center,
                            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    SelectionDialogCompose(listItem = listOf<String>(), {}, {})
}