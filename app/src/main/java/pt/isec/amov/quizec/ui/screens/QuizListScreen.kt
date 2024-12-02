package pt.isec.amov.quizec.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.amov.quizec.model.quiz.Quiz


@Composable
fun QuizListScreen(
    quizList: List<Quiz>,
    onSelectQuiz: (Quiz) -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate("createQuiz") },
            modifier = Modifier.padding(16.dp),
        ) {
            Text(text = "Create Quiz")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = quizList,
                key = { quiz -> quiz.hashCode() }
            ) { quiz ->
                QuizCard(
                    quiz = quiz,
                    onSelectQuiz = onSelectQuiz
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizCard(
    quiz: Quiz,
    onSelectQuiz: (Quiz) -> Unit
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
            modifier = Modifier
                .padding(8.dp),
        ) {
            Text(
                text = quiz.title,
                fontSize = 20.sp
            )
            quiz.questions.forEach { question ->
                /*Text(
                    text = question.title,
                    fontSize = 16.sp
                )*/
                QuestionCard(
                    question = question,
                    onSelectQuestion = { question ->
                        //Log.d("Question selected", question.title)
                    }
                )
            }
        }
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text("Quiz Option 1") },
            onClick = {
                expanded = false
            }
        )
        DropdownMenuItem(
            text = { Text("Quiz Option 2") },
            onClick = {
                expanded = false
            }
        )
    }
}
