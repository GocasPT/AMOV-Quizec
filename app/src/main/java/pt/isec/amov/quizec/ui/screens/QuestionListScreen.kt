package pt.isec.amov.quizec.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
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
import pt.isec.amov.quizec.ui.components.CustomList

@Composable
fun QuestionListScreen(
    questionList: List<Question>,
    onSelectQuestion: (Question) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO: go to "ManageQuestion" to create new question*/ }) {
            Text("+")
        }
        CustomList(items = questionList,
            onSelectItem = { question -> onSelectQuestion(question as Question) }) { question, onSelect ->
            QuestionCard(question = question as Question, onSelectQuestion = { onSelect(question) })
        }
    }
}

@Composable
fun QuestionCard(
    question: Question, onSelectQuestion: (Question) -> Unit
) {
    Card(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(192, 255, 255)
        ),
        onClick = {
            onSelectQuestion(question)
        }) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                question.image?.let {
                    //TODO: get image from string and "return" image/file
                    //PLACE_HOLDER
                    Icon(Icons.Filled.AccountCircle, "Question image")
                    Spacer(modifier = Modifier.padding(8.dp))
                }
                Text(
                    text = question.title, fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Type: ${question.type.string}", fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
fun QuestListPreview() {
    QuestionListScreen(questionList = listOf(
        Question(
            "Question 1",
            QuestionType.YES_NO,
            null,
            listOf(QuestionOption("Yes"), QuestionOption("No"))
        ), Question(
            "Question 2",
            QuestionType.MULTIPLE_CHOICE,
            null,
            listOf(QuestionOption("Option 1"), QuestionOption("Option 2"))
        ), Question(
            "Question 3",
            QuestionType.MULTIPLE_CHOICE,
            "batata.jpg",
            listOf(QuestionOption("Option 1"), QuestionOption("Option 2"))
        )
    ), onSelectQuestion = {})
}