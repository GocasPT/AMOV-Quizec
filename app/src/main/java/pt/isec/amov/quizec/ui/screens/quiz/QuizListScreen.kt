package pt.isec.amov.quizec.ui.screens.quiz

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.ui.components.CustomList

@Composable
fun QuizListScreen(
    quizList: List<Quiz>,
    onSelectQuiz: (Quiz) -> Unit,
    onCreateQuiz: () -> Unit,
    onEditQuiz: (Quiz) -> Unit,
    onDeleteQuiz: (Quiz) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onCreateQuiz() }) {
            Text("+")
        }
        CustomList(
            items = quizList,
            onSelectItem = { quiz -> onSelectQuiz(quiz as Quiz) }) { quiz, onSelect ->
            QuizCard(
                quiz = quiz as Quiz,
                onSelectQuiz = { onSelect(quiz) },
                onEditQuiz = { onEditQuiz(quiz) },
                onDeleteQuiz = { onDeleteQuiz(quiz) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizCard(
    quiz: Quiz,
    onSelectQuiz: (Quiz) -> Unit,
    onEditQuiz: (Quiz) -> Unit,
    onDeleteQuiz: (Quiz) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .combinedClickable(
                onClick = { onSelectQuiz(quiz) },
                onLongClick = { expanded = true }
            ),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(255, 255, 192)
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                quiz.image?.let {
                    //TODO: get image from string and "return" image/file
                    //PLACE_HOLDER
                    Icon(Icons.Filled.AccountCircle, it)
                    Spacer(modifier = Modifier.padding(8.dp))
                }
                Text(
                    text = quiz.title, fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                /*
                quiz.maxTime?.let {
                    Text(text = "$it min", fontSize = 16.sp)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Icon(Icons.Rounded.Info, "Max Time", Modifier.size(18.dp))
                }
                if (quiz.locationRestricted)
                    Icon(Icons.Filled.LocationOn, "Location Restricted", Modifier.size(18.dp))
                if (quiz.immediateResults)
                    Icon(Icons.Filled.Menu, "Immediate Results", Modifier.size(18.dp))
                 */
            }
            quiz.questions?.forEach { question ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Filled.PlayArrow, "Bullet Point", Modifier.size(18.dp))
                    Text(
                        text = "${question.content} - Type ${question.answers.answerType.displayName}",
                        fontSize = 16.sp
                    )
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("View TODO") },
                onClick = {
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = {
                    expanded = false
                    onEditQuiz(quiz)
                }
            )
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    expanded = false
                    onDeleteQuiz(quiz)
                }
            )
        }
    }
}