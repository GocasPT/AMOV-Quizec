package pt.isec.amov.quizec.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        onSelectItem = { quiz -> onSelectQuiz(quiz as Quiz) }
    ) { quiz, onSelect ->
        QuizCard(
            quiz = quiz as Quiz,
            onSelectQuiz = { onSelect(quiz) }
        )
    }
}

@Composable
fun QuizCard(
    quiz: Quiz,
    onSelectQuiz: (Quiz) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(255, 255, 192)
        ),
        onClick = {
            onSelectQuiz(quiz)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
        ) {
            Row {
                quiz.image?.let {
                    //TODO: get image from string and "return" image/file
                    //PLACE_HOLDER
                    Icon(Icons.Filled.AccountCircle, null)
                    Spacer(modifier = Modifier.padding(8.dp))
                }
                Text(
                    text = quiz.title,
                    fontSize = 20.sp
                )
            }
            quiz.questions.forEach { question ->
                Row {
                    Text(
                        text = question.title,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Type ${question.type}",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun QuizLIstPreview() {
    QuizListScreen(
        quizList = listOf(
            Quiz(
                "Quiz 1",
                null,
                listOf(
                    Question(
                        "Question 1",
                        QuestionType.YES_NO,
                        null,
                        listOf(QuestionOption("Yes"), QuestionOption("No"))
                    ),
                    Question(
                        "Question 2",
                        QuestionType.MULTIPLE_CHOICE,
                        null,
                        listOf(
                            QuestionOption("Option 1"), QuestionOption("Option 2")
                        )
                    )
                ), false, null, false, false
            ),
            Quiz(
                "Quiz 2",
                null,
                listOf(
                    Question(
                        "Question 1",
                        QuestionType.YES_NO,
                        null,
                        listOf(QuestionOption("Yes"), QuestionOption("No"))
                    ),
                    Question(
                        "Question 2",
                        QuestionType.MULTIPLE_CHOICE,
                        null,
                        listOf(
                            QuestionOption("Option 1"), QuestionOption("Option 2")
                        )
                    )
                ), false, null, false, false
            ),
            Quiz(
                "Quiz 3",
                "batata.png",
                listOf(
                    Question(
                        "Question 1",
                        QuestionType.YES_NO,
                        null,
                        listOf(QuestionOption("Yes"), QuestionOption("No"))
                    ),
                    Question(
                        "Question 2",
                        QuestionType.MULTIPLE_CHOICE,
                        null,
                        listOf(
                            QuestionOption("Option 1"), QuestionOption("Option 2")
                        )
                    )
                ), false, null, false, false
            )
        ),
        onSelectQuiz = {
            Log.d("Quiz selected", it.title)
        }
    )
}
