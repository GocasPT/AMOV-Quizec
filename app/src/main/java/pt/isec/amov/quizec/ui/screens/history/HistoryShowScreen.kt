package pt.isec.amov.quizec.ui.screens.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.history.History
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.stringResource
import pt.isec.amov.quizec.R

@Composable
fun HistoryShowScreen(
    history: History,
    modifier: Modifier = Modifier,
) {

    val scoreValue = "${history.score}/20"
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            Text(
                text = stringResource(R.string.user_id, history.userId),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        item {
            Text(
                text = stringResource(R.string.quiz, history.quiz.title),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        item {
            Text(
                text = stringResource(R.string.score, scoreValue),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        item {
            Text(
                text = stringResource(R.string.date, history.date),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        items(history.answers) { answer ->
            Card(
                modifier = Modifier.padding(8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.answer, answer.toString()),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}