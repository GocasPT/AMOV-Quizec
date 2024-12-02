package pt.isec.amov.quizec.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.Question
import pt.isec.amov.quizec.model.QuestionOption
import pt.isec.amov.quizec.model.QuestionType
import pt.isec.amov.quizec.model.Quiz

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen(
    questionList : List<Question>,
    saveQuiz: (Quiz) -> Unit
) {
    var quizTitle by remember { mutableStateOf("") }
    var maxTimeMinutes by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }
    var locationRestricted by remember { mutableStateOf(false) }
    var immediateResults by remember { mutableStateOf(true) }
    var selectedQuestions = remember { mutableStateListOf<Question>() }

    val hardcodedQuiz = Quiz(
        "Quiz ADD", "ADD.png", listOf(
            Question(
                "Question 1", QuestionType.SINGLE_CHOICE, "null.png", listOf(
                    QuestionOption("ADD"),
                    QuestionOption("ADD"),
                    QuestionOption("ADD"),
                    QuestionOption("ADD"),
                )
            ),
            Question(
                "Question 2", QuestionType.FILL_BLANK, null, listOf(
                    QuestionOption("ADD"),
                    QuestionOption("ADD2"),
                )
            ),
            Question(
                "Question 3", QuestionType.ASSOCIATION, null, listOf(
                    QuestionOption("ADD"),
                    QuestionOption("ADD"),
                    QuestionOption("ADD"),
                    QuestionOption("REMOVE"),
                )
            ),
        ), false, 69, true, true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create New Quiz",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = quizTitle,
            onValueChange = { quizTitle = it },
            label = { Text("Quiz Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = maxTimeMinutes,
            onValueChange = {
                if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                    maxTimeMinutes = it
                }
            },
            label = { Text("Max Time (minutes)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Start Immediately")
            Switch(
                checked = isActive,
                onCheckedChange = { isActive = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Restrict Location")
            Switch(
                checked = locationRestricted,
                onCheckedChange = { locationRestricted = it }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Show Results Immediately")
            Switch(
                checked = immediateResults,
                onCheckedChange = { immediateResults = it }
            )
        }

        QuestionList(
            availableQuestions = questionList,
            selectedQuestions = selectedQuestions,
            onToggleQuestion = { question ->
                if (selectedQuestions.contains(question)) {
                    selectedQuestions.remove(question)
                } else {
                    selectedQuestions.add(question)
                }
            }
        )

        Button(
            onClick = {
                saveQuiz(hardcodedQuiz)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save Quiz")
        }
    }
}

@Composable
fun QuestionList(
    availableQuestions: List<Question>,
    selectedQuestions: List<Question>,
    onToggleQuestion: (Question) -> Unit
) {
    LazyColumn(
        modifier = Modifier
    ) {
        items(
            items = availableQuestions,
            key = { question -> question.hashCode() }
        ) { question ->
            val isSelected = selectedQuestions.contains(question)

            QuestionCard(
                question = question,
                isSelected = isSelected,
                onToggle = { onToggleQuestion(question) }
            )
        }
    }
}

@Composable
fun QuestionCard(
    question: Question,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = question.title,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (isSelected) Icons.Default.Remove else Icons.Default.Add,
                    contentDescription = if (isSelected) "Remove" else "Add"
                )
            }
        }
    }
}