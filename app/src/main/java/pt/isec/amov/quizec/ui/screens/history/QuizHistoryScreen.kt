package pt.isec.amov.quizec.ui.screens.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.history.History
import pt.isec.amov.quizec.ui.components.CustomList

@Composable
fun QuizHistoryScreen(
    onCreateDummy : () -> Unit,
    onSelectHistory : (History) -> Unit,
    historyList: List<History>,
) {

    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filterOptions = listOf("True / False", "Single Choice")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DropdownMenu(
                expanded = false,
                onDismissRequest = {},
                modifier = Modifier.weight(1f)
            ) {
                filterOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedFilter = option
                            //onFilter(option)
                        }
                    )
                }
            }

            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    //onSearch(it)
                },
                modifier =
                Modifier
                    .weight(1f),
                placeholder = { Text("Search") },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Search, "Search")
                }
            )
        }



        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomList(
                items = historyList,
                onSelectItem = { history -> onSelectHistory(history as History) }) { history, onSelect ->
                QuizHistoryCard(
                    history = history as History,
                    onSelectHistory = { onSelect(history) }
                )
            }
            //Temporary button to create dummy data
            //This does create but need to refresh the screen to see the changes
            Button(onClick = { onCreateDummy() }) {
                Text("Create Dummy")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizHistoryCard(
    history: History,
    onSelectHistory: (History) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .combinedClickable(
                onClick = { onSelectHistory(history) },
            ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Box(
            modifier = Modifier
                .height(160.dp)
        ) {
            history.quiz.image?.let {
                Image(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.6f)
                                )
                            )
                        )
                        .fillMaxSize(),
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Quiz Image",
                    contentScale = ContentScale.FillWidth
                )
            } ?: Image(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
                    .fillMaxSize(),
                painter = painterResource(R.drawable.quizec_1080),
                contentDescription = "Quiz Image",
                contentScale = ContentScale.FillWidth
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = history.quiz.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }
    }
}
