package pt.isec.amov.quizec.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.amov.quizec.model.Question
import pt.isec.amov.quizec.model.QuestionOption
import pt.isec.amov.quizec.model.QuestionType
import pt.isec.amov.quizec.model.Quiz
import pt.isec.amov.quizec.ui.components.CustomList

@Composable
fun QuizListScreen(
    quizList: List<Quiz>,
    onSelectQuiz: (Quiz) -> Unit,
) {
    CustomList(
        items = quizList,
        onSelectItem = { quiz -> onSelectQuiz(quiz as Quiz) }) { quiz, onSelect ->
        QuizCard(quiz = quiz as Quiz, onSelectQuiz = { onSelect(quiz) })
    }
}

@Composable
fun QuizCard(
    quiz: Quiz, onSelectQuiz: (Quiz) -> Unit
) {
    Card(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(255, 255, 192)
        ),
        onClick = {
            onSelectQuiz(quiz)
        }) {
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
                quiz.maxTime?.let {
                    Text(text = "$it min", fontSize = 16.sp)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Icon(Icons.Rounded.Info, "Max Time", Modifier.size(18.dp))
                }
                if (quiz.locationRestricted)
                    Icon(Icons.Filled.LocationOn, "Location Restricted", Modifier.size(18.dp))
                if (quiz.immediateResults)
                    Icon(Icons.Filled.Menu, "Immediate Results", Modifier.size(18.dp))
            }
            quiz.questions.forEach { question ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Filled.PlayArrow, "Bullet Point", Modifier.size(18.dp))
                    Text(
                        text = "${question.title} - Type ${question.type}", fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun QuizLIstPreview() {
    QuizListScreen(quizList = listOf(
        Quiz(
            "Quiz 1", null, listOf(
                Question(
                    "Question 1",
                    QuestionType.YES_NO,
                    null,
                    listOf(QuestionOption("Yes"), QuestionOption("No"))
                ), Question(
                    "Question 2", QuestionType.MULTIPLE_CHOICE, null, listOf(
                        QuestionOption("Option 1"), QuestionOption("Option 2")
                    )
                )
            ), false, null, false, false
        ), Quiz(
            "Quiz 2", null, listOf(
                Question(
                    "Question 1",
                    QuestionType.MATCHING,
                    null,
                    listOf(QuestionOption("Yes"), QuestionOption("No"))
                ), Question(
                    "Question 2", QuestionType.ORDERING, null, listOf(
                        QuestionOption("Option 1"), QuestionOption("Option 2")
                    )
                )
            ), false, null, true, false
        ), Quiz(
            "Quiz 3", "batata.png", listOf(
                Question(
                    "Question 1",
                    QuestionType.FILL_BLANK,
                    null,
                    listOf(QuestionOption("Yes"), QuestionOption("No"))
                ), Question(
                    "Question 2", QuestionType.ASSOCIATION, null, listOf(
                        QuestionOption("Option 1"), QuestionOption("Option 2")
                    )
                )
            ), false, null, false, true
        ), Quiz(
            "Quiz 4", null, listOf(
                Question(
                    "Question 1",
                    QuestionType.YES_NO,
                    null,
                    listOf(QuestionOption("Yes"), QuestionOption("No"))
                ), Question(
                    "Question 2", QuestionType.MULTIPLE_CHOICE, null, listOf(
                        QuestionOption("Option 1"), QuestionOption("Option 2")
                    )
                )
            ), false, 30, false, false
        )
    ), onSelectQuiz = {})
}
