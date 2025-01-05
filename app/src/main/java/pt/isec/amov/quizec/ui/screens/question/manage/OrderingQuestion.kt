package pt.isec.amov.quizec.ui.screens.question.manage

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Answer.Matching
import pt.isec.amov.quizec.model.question.Answer.Ordering
import pt.isec.amov.quizec.model.question.Question

@Composable
fun OrderingQuestion(
    initialAnswer: Ordering,
    onAnswerChanged: (Answer) -> Unit,
    saveEnabled: (Boolean) -> Unit,
) {
    var newAnswer by remember { mutableStateOf("") }
    var answers by remember { mutableStateOf(initialAnswer.order) }

    LaunchedEffect(answers) {
        Log.d("OrderingQuestion", "answers: $answers")
        onAnswerChanged(Ordering(answers))
        saveEnabled(answers.size >= 2)
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
                if (newAnswer.isNotBlank() && !answers.contains(newAnswer)) {
                    answers = answers + newAnswer
                }
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                            Icon(
                                Icons.Filled.ArrowUpward,
                                contentDescription = "Arrow Upward"
                            )
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
                            Icon(
                                Icons.Filled.ArrowDownward,
                                contentDescription = "Arrow Downward"
                            )
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



@Composable
fun OrderingQuestionDisplay(
    order: List<String>,
    onOrderChange: (List<String>) -> Unit
) {
    var currentOrder by remember { mutableStateOf(order.shuffled()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        currentOrder.forEachIndexed { index, answer ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${index + 1}. $answer",
                    modifier = Modifier.weight(1f)
                )
                if (index > 0) {
                    IconButton(
                        onClick = {
                            val newOrder = currentOrder.toMutableList().apply {
                                val temp = this[index]
                                this[index] = this[index - 1]
                                this[index - 1] = temp
                            }
                            currentOrder = newOrder
                            onOrderChange(newOrder)
                        }
                    ) {
                        Icon(
                            Icons.Filled.ArrowUpward,
                            contentDescription = "Move Up"
                        )
                    }
                }

                if (index < currentOrder.size - 1) {
                    IconButton(
                        onClick = {
                            val newOrder = currentOrder.toMutableList().apply {
                                val temp = this[index]
                                this[index] = this[index + 1]
                                this[index + 1] = temp
                            }
                            currentOrder = newOrder
                            onOrderChange(newOrder)
                        }
                    ) {
                        Icon(
                            Icons.Filled.ArrowDownward,
                            contentDescription = "Move Down"
                        )
                    }
                }
            }
        }
    }
}
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        randomOrder.forEachIndexed { index, answer ->
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "${index + 1}. $answer",
//                    modifier = Modifier.weight(1f)
//                )
//                IconButton(
//                    onClick = {
//                        if (!selectedOrder.value.contains(answer)) {
//                            selectedOrder.value = selectedOrder.value.toMutableList().apply {
//                                add(answer)
//                            }
//                            onOrderChange(selectedOrder.value)
//                        }
//                    }
//                ) {
//                    Icon(Icons.Filled.Add, contentDescription = "Add to Order")
//                }
//            }
//        }
//    }
