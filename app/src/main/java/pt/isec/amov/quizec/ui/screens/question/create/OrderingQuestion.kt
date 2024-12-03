package pt.isec.amov.quizec.ui.screens.question.create

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.question.Answer

@Composable
fun OrderingQuestion(
    onAnswerChanged: (Answer) -> Unit,
    saveEnabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    var newAnswer by remember { mutableStateOf("") }
    var answers by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(answers) {
        onAnswerChanged(Answer.Ordering(answers.toSet()))
        saveEnabled(answers.size >= 2)
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
                if (newAnswer.isNotBlank() && !answers.contains(newAnswer)) {
                    answers = answers + newAnswer
                }
                newAnswer = ""
            },
            enabled = newAnswer.isNotBlank()
        ) {
            Text("+")
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        answers.forEachIndexed { index, answer ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${index + 1}. $answer", modifier = Modifier.weight(1f))
                Row {
                    if (index > 0) {
                        IconButton(onClick = {
                            val newOrder = answers.toMutableList().apply {
                                val temp = this[index]
                                this[index] = this[index - 1]
                                this[index - 1] = temp
                            }
                            answers = newOrder
                        }) {
                            Text("↑")
                        }
                    }
                    if (index < answers.size - 1) {
                        IconButton(onClick = {
                            val newOrder = answers.toMutableList().apply {
                                val temp = this[index]
                                this[index] = this[index + 1]
                                this[index + 1] = temp
                            }
                            answers = newOrder
                        }) {
                            Text("↓")
                        }
                    }
                    IconButton(
                        onClick = { answers = answers.filterIndexed { idx, _ -> idx != index } }
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}
