package pt.isec.amov.quizec.ui.screens.question.manage

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Answer.SingleChoice

@Composable
fun SingleChoiceQuestion(
    initialAnswer: SingleChoice,
    onAnswerChanged: (Answer) -> Unit,
    saveEnabled: (Boolean) -> Unit,
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
    )
}

@Composable
fun AnswerEntrySingleChoice(
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

@Composable
fun SingleChoiceDisplay(
    answers: Set<Pair<Boolean, String>>,
    selectedOption: Pair<Boolean, String>?,
    onOptionSelected: (Pair<Boolean, String>) -> Unit,
) {
    Column(
        modifier = Modifier.verticalScroll(ScrollState(0))
    ) {
        answers.forEach { answer ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onOptionSelected(answer) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = answer == selectedOption,
                    onClick = { onOptionSelected(answer) }
                )
                Text(
                    text = answer.second,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SingleChoiceDisplayPreview() {
    SingleChoiceDisplay(
        answers = setOf(
            Pair(true, "Option 1"),
            Pair(false, "Option 2"),
            Pair(false, "Option 3"),
        ),
        selectedOption = Pair(false, "Option 1"),
        onOptionSelected = {}
    )
}