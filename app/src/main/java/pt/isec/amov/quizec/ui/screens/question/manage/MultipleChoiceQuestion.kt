package pt.isec.amov.quizec.ui.screens.question.manage

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Answer.MultipleChoice

@Composable
fun MultipleChoiceQuestion(
    initialAnswer: MultipleChoice,
    onAnswerChanged: (Answer) -> Unit,
    saveEnabled: (Boolean) -> Unit,
) {
    var newAnswer by remember { mutableStateOf("") }
    var answers by remember { mutableStateOf(initialAnswer.answers) }

    LaunchedEffect(answers) {
        onAnswerChanged(MultipleChoice(answers))
        saveEnabled(answers.count { it.first } >= 2)
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newAnswer,
                onValueChange = { newAnswer = it },
                label = { Text(stringResource(R.string.add_option)) },
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
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnswerEntryMultipleChoice(
            answers = answers,
            onClick = { answer ->
                answers = answers.map {
                    if (it.second == answer.second) it.copy(first = !it.first) else it
                }.toSet()
            },
            onOptionDelete = { answer ->
                answers = answers - answer
            }
        )
    }
}

@Composable
fun AnswerEntryMultipleChoice(
    answers: Set<Pair<Boolean, String>>,
    onClick: (Pair<Boolean, String>) -> Unit,
    onOptionDelete: (Pair<Boolean, String>) -> Unit,
) {
    Column {
        answers.forEach { answer ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onClick(answer) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = answer.first,
                    onCheckedChange = { onClick(answer) }
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

@Composable
fun MultipleChoiceDisplay(
    answers: Set<Pair<Boolean, String>>,
    selectedOptions: Set<Pair<Boolean, String>>,
    onOptionsSelected: (Set<Pair<Boolean, String>>) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .verticalScroll(ScrollState(0))
    ) {
        answers.forEach { answer ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onOptionsSelected(setOf(answer)) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selectedOptions.contains(answer),
                    onCheckedChange = { onOptionsSelected(setOf(answer)) }
                )
                Text(
                    text = answer.second,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}