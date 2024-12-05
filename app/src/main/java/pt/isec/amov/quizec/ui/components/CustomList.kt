package pt.isec.amov.quizec.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomList(
    modifier: Modifier = Modifier,
    items: List<Any>,
    onSelectItem: (Any) -> Unit,
    card: @Composable (Any, (Any) -> Unit) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
            items = items,
        ) { item ->
            card(
                item,
                onSelectItem
            )
        }
    }
}