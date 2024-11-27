package pt.isec.amov.quizec.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.amov.quizec.model.Question

@Composable
fun QuestionListScreen(
    questionList: List<Question>,
    onSelectQuestion: (Question) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
    ) {
        items(
            items = questionList,
            key = { question -> question.hashCode() }
        ) { question ->
            QuestionCard(
                question = question,
                onSelectQuestion = onSelectQuestion
            )
        }
    }
}

@Composable
fun QuestionCard(
    question: Question,
    onSelectQuestion: (Question) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(192, 255, 255)
        ),
        onClick = {
            onSelectQuestion(question)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
        )  {
            Text(
                text = question.title,
                fontSize = 20.sp
            )
            Text(
                text = question.type.name,
                fontSize = 16.sp
            )
            question.options.forEach { option ->
                Text(
                    text = option.text,
                    fontSize = 12.sp
                )
            }
        }
    }
}