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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.amov.quizec.model.question.Question

@Composable
fun QuestionListScreen(
    questionList: List<Question>,
    onSelectQuestion: (Question) -> Unit,
    navController: NavController
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate("createQuestion") },
            modifier = Modifier.padding(16.dp),
        ) {
            Text(text = "Create Question")
        }

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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionCard(
    question: Question,
    onSelectQuestion: (Question) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .combinedClickable(
                onClick = { onSelectQuestion(question) },
                onLongClick = { expanded = true }
            ),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(192, 255, 255)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
        )  {
            Text(
                text = question.content,
                fontSize = 20.sp
            )
            Text(
                text = question.type.name,
                fontSize = 16.sp
            )
            question.answers.forEach { option ->
                Text("Answer: $option")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Question Option 1") },
                    onClick = {
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Question Option 2") },
                    onClick = {
                        expanded = false
                    }
                )
            }
        }
    }
}