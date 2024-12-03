package pt.isec.amov.quizec.ui.screens.quiz.manage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.quiz.Quiz

private fun generateRandomCode(length: Int = 5): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

@Composable
fun ManageQuizScreen(
    quiz: Quiz?,
    questionList: List<Question>,
    saveQuiz: (Quiz) -> Unit
) {
    var quizTitle by remember { mutableStateOf(quiz?.title ?: "") }
    var maxTimeMinutes by remember { mutableStateOf(quiz?.maxTime?.toString() ?: "") }
    var isActive by remember { mutableStateOf(quiz?.isActive ?: true) }
    var locationRestricted by remember { mutableStateOf(quiz?.locationRestricted ?: false) }
    var immediateResults by remember { mutableStateOf(quiz?.immediateResults ?: true) }
    val selectedQuestions = remember { mutableStateListOf<Question>().apply {
        quiz?.questions?.let { addAll(it) }
    }}
    val scrollState = rememberScrollState()

    fun isFormValid(): Boolean {
        return quizTitle.isNotEmpty() &&
                maxTimeMinutes.isNotEmpty() &&
                maxTimeMinutes.all { it.isDigit() } &&
                selectedQuestions.isNotEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (quiz == null) "New Quiz" else "Edit Quiz",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
            fontWeight = FontWeight.Bold
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

        Text(
            text = "Select Questions",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = questionList,
                key = { it.hashCode() }
            ) { question ->
                QuestionCard(
                    question = question,
                    isSelected = selectedQuestions.contains(question),
                    onToggle = {
                        if (selectedQuestions.contains(question)) {
                            selectedQuestions.remove(question)
                        } else {
                            selectedQuestions.add(question)
                        }
                    }
                )
            }
        }

        Button(
            onClick = {
                val updatedQuiz = quiz?.copy(
                    title = quizTitle,
                    questions = selectedQuestions,
                    isActive = isActive,
                    maxTime = maxTimeMinutes.toLongOrNull(),
                    locationRestricted = locationRestricted,
                    immediateResults = immediateResults
                ) ?: Quiz(
                    id = generateRandomCode(),
                    title = quizTitle,
                    image = null,
                    questions = selectedQuestions,
                    isActive = isActive,
                    maxTime = maxTimeMinutes.toLongOrNull(),
                    locationRestricted = locationRestricted,
                    immediateResults = immediateResults
                )
                saveQuiz(updatedQuiz)
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            enabled = isFormValid()
        ) {
            Text(if (quiz == null) "Create Quiz" else "Save Changes")
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
                text = question.content,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = question.type.toString(),
                modifier = Modifier.padding(end = 16.dp)
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