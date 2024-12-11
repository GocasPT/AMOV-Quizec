package pt.isec.amov.quizec.ui.screens.question.manage

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
import pt.isec.amov.quizec.model.question.Answer.*

@Composable
fun SingleChoiceQuestion(
    initialAnswer: SingleChoice,
    onAnswerChanged: (Answer) -> Unit,
    saveEnabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    var newAnswer by remember { mutableStateOf("") }
    var answers by remember { mutableStateOf(initialAnswer.answers) }

    LaunchedEffect(answers) {
        onAnswerChanged(SingleChoice(answers))
        saveEnabled(answers.size > 1 && answers.any { it.first })
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
                answers = answers + Pair(false, newAnswer)
                newAnswer = ""
            },
            enabled = newAnswer.isNotBlank()
        ) {
            Text("+")
        }
    }

    AnswerEntrySingleChoice(
        answers = answers,
        onClick = { answer ->
            answers = answers.map {
                if (it.second == answer.second) it.copy(first = true)
                else it.copy(first = false)
            }.toSet()
        },
        onOptionDelete = { answer ->
            answers = answers - answer
        },
        modifier = modifier,
        scrollState = scrollState
    )
}

@Composable
fun AnswerEntrySingleChoice(
    answers: Set<Pair<Boolean, String>>,
    onClick: (Pair<Boolean, String>) -> Unit,
    onOptionDelete: (Pair<Boolean, String>) -> Unit,
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
                RadioButton(
                    selected = answer.first,
                    onClick = { onClick(answer) }
                )
                Text(
                    text = answer.second,
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