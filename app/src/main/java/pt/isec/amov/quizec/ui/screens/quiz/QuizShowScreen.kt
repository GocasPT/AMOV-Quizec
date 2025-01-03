package pt.isec.amov.quizec.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.ui.components.CustomList
import pt.isec.amov.quizec.ui.screens.question.QuestionCard

@Composable
fun QuizShowScreen(
    quiz: Quiz,
    onCreateLobby: (Long) -> Unit,
    onCreateQuestion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = quiz.title,
                modifier = Modifier.weight(1f),
            )
            Button(
                onClick = { onCreateLobby(quiz.id!!.toLong()) }
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Create Lobby")
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            onClick = onCreateQuestion
        ) {
            Icon(Icons.Default.Add, contentDescription = "Create Question")
        }
        CustomList(
            items = quiz.questions ?: emptyList(),
            onSelectItem = { }
        ) { question, _ ->
            QuestionCard(
                question = question as Question,
                onSelectQuestion = {},
                onEditQuestion = {},
                onDeleteQuestion = {}
            )
        }
        //TODO: Show all details
    }
}

@Preview(showBackground = true)
@Composable
fun QuizShowScreenPreview() {
    QuizShowScreen(
        quiz = Quiz(
            title = "Quiz Title",
            owner = "Owner",
            id = null,
            image = null,
            questions = listOf(
                Question(null, "Question 1", null, Answer.TrueFalse(true), "Sr. batata"),
                Question(null, "Question 2", null, Answer.SingleChoice(setOf()), "Sr. batata"),
                Question(null, "Question 3", null, Answer.TrueFalse(true), "Sr. batata"),
            )
        ),
        onCreateLobby = {},
        onCreateQuestion = {}
    )
}