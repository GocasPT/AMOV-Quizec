package pt.isec.amov.quizec.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Answer.TrueFalse
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestionScreen(
    saveQuestion: (Question) -> Unit
) {
    var questionTitle by remember { mutableStateOf("") }
    var questionType by remember { mutableStateOf<QuestionType>(QuestionType.YES_NO) }
    var questionAnswers by remember { mutableStateOf<Answer>(TrueFalse(false)) }
    var isExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var saveEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "New Question",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = questionTitle,
            onValueChange = { questionTitle = it },
            label = { Text("Question Text") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            OutlinedTextField(
                value = questionType.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Question Type") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        Modifier.clickable { isExpanded = !isExpanded }
                    )
                },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                QuestionType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            questionType = type
                            isExpanded = false
                        }
                    )
                }
            }
        }

        when (questionType) {
            QuestionType.YES_NO -> {
                var trueFalseAnswer by remember { mutableStateOf(false) }

                LaunchedEffect(trueFalseAnswer) {
                    questionAnswers = TrueFalse(trueFalseAnswer)
                    saveEnabled = true
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("False")
                    Switch(
                        checked = trueFalseAnswer,
                        onCheckedChange = { trueFalseAnswer = it }
                    )
                    Text("True")
                }
            }
            QuestionType.SINGLE_CHOICE -> {
                var newAnswer by remember { mutableStateOf("") }
                var answers by remember { mutableStateOf(setOf<String>()) }
                var rightAnswer by remember { mutableStateOf("") }

                LaunchedEffect(answers, rightAnswer) {
                    questionAnswers = Answer.SingleChoice(answers, rightAnswer)
                    saveEnabled = answers.size >= 2 && rightAnswer.isNotBlank()
                }

                LaunchedEffect(answers) {
                    if (rightAnswer.isNotBlank() && !answers.contains(rightAnswer)) rightAnswer = ""
                }

                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newAnswer,
                        onValueChange = { newAnswer = it },
                        label = { Text("Add Option") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            answers = answers + newAnswer
                            newAnswer = ""
                        },
                        enabled = newAnswer.isNotBlank()
                    ) {
                        Text("+")
                    }
                }
                AnswerEntry1(
                    answers = answers,
                    onClick = { rightAnswer = it },
                    onOptionDelete = { answer ->
                        answers = answers - answer
                        if (answer == rightAnswer) {
                            rightAnswer = ""
                        }
                    },
                    rightAnswer = rightAnswer,
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                )
            }
            QuestionType.MULTIPLE_CHOICE -> {
                var newAnswer by remember { mutableStateOf("") }
                var answers by remember { mutableStateOf(setOf<String>()) }
                var rightAnswers by remember { mutableStateOf(setOf<String>()) }

                LaunchedEffect(answers, rightAnswers) {
                    questionAnswers = Answer.MultipleChoice(answers, rightAnswers)
                    saveEnabled = answers.size >= 2 && rightAnswers.isNotEmpty() && rightAnswers.size >= 2
                    Log.d("CreateQuestionScreen", "saveEnabled: $saveEnabled")
                }

                LaunchedEffect(answers) {
                    rightAnswers.forEach { rightAnswer ->
                        if (!answers.contains(rightAnswer)) rightAnswers = rightAnswers - rightAnswer
                    }
                }

                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newAnswer,
                        onValueChange = { newAnswer = it },
                        label = { Text("Add Option") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            answers = answers + newAnswer
                            newAnswer = ""
                        },
                        enabled = newAnswer.isNotBlank()
                    ) {
                        Text("+")
                    }
                }
                AnswerEntry2(
                    answers = answers,
                    onClick = { rightAnswers = if (rightAnswers.contains(it)) rightAnswers - it else rightAnswers + it },
                    onOptionDelete = { answer ->
                        answers = answers - answer
                        rightAnswers = rightAnswers - answer
                    },
                    rightAnswer = rightAnswers,
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState)
                )
            }
            else -> {
                // TODO: All the other question types
            }
        }

        Button(
            onClick = {
                saveQuestion(Question(questionTitle, questionType, null, listOf(questionAnswers)))
            },
            modifier = Modifier.padding(top = 16.dp),
            enabled = saveEnabled && questionTitle.isNotBlank()
        ) {
            Text("Save question")
        }
    }
}

@Composable
fun AnswerEntry1(
    answers: Set<String>,
    onClick: (String) -> Unit,
    onOptionDelete: (String) -> Unit,
    rightAnswer : String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ){
        answers.forEach { answer ->
            val backgroundColor = if (answer == rightAnswer) Color.Green else Color.Gray

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onClick(answer) },
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = answer,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onOptionDelete(answer) }
                    ) {
                        Text("-")
                    }
                }
            }
        }
    }
}

@Composable
fun AnswerEntry2(
    answers: Set<String>,
    onClick: (String) -> Unit,
    onOptionDelete: (String) -> Unit,
    rightAnswer : Set<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ){
        answers.forEach { answer ->
            val backgroundColor = if (rightAnswer.contains(answer)) Color.Green else Color.Gray

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onClick(answer) },
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = answer,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onOptionDelete(answer) }
                    ) {
                        Text("-")
                    }
                }
            }
        }
    }
}