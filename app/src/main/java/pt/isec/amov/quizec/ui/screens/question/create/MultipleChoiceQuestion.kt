package pt.isec.amov.quizec.ui.screens.question.create

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Answer.MultipleChoice

@Composable
fun MultipleChoiceQuestion(
    onAnswerChanged: (Answer) -> Unit,
    saveEnabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    var newAnswer by remember { mutableStateOf("") }
    var answers by remember { mutableStateOf(setOf<String>()) }
    var rightAnswers by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(answers, rightAnswers) {
        onAnswerChanged(MultipleChoice(answers, rightAnswers))
        saveEnabled(answers.size >= 2 && rightAnswers.isNotEmpty() && rightAnswers.size >= 2)
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
    AnswerEntryMultipleChoice(
        answers = answers,
        onClick = { answer ->
            if (rightAnswers.contains(answer)) rightAnswers = rightAnswers - answer
            else rightAnswers = rightAnswers + answer
        },
        onOptionDelete = { answer ->
            answers = answers - answer
            rightAnswers = rightAnswers - answer
        },
        rightAnswers = rightAnswers,
        modifier = modifier,
        scrollState = scrollState
    )
}

@Composable
fun AnswerEntryMultipleChoice(
    answers: Set<String>,
    onClick: (String) -> Unit,
    onOptionDelete: (String) -> Unit,
    rightAnswers: Set<String>,
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    Column(modifier = modifier.verticalScroll(scrollState)) {
        answers.forEach { answer ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onClick(answer) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rightAnswers.contains(answer),
                    onCheckedChange = { onClick(answer) }
                )
                Text(
                    text = answer,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onOptionDelete(answer) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Option"
                    )
                }
            }
        }
    }
}