package pt.isec.amov.quizec.ui.screens.question.create

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
import pt.isec.amov.quizec.model.question.Answer.Matching

@Composable
fun MatchingQuestion(
    onAnswerChanged: (Answer) -> Unit,
    saveEnabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    var leftSide by remember { mutableStateOf("") }
    var rightSide by remember { mutableStateOf("") }
    var matchingPairs by remember { mutableStateOf<Set<Pair<String, String>>>(emptySet()) }

    LaunchedEffect(matchingPairs) {
        onAnswerChanged(Matching(matchingPairs))
        saveEnabled(matchingPairs.isNotEmpty())
    }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = leftSide,
                onValueChange = { leftSide = it },
                label = { Text("Left Side") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = rightSide,
                onValueChange = { rightSide = it },
                label = { Text("Right Side") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (leftSide.isNotEmpty() && rightSide.isNotEmpty()) {
                        val newPair = Pair(leftSide, rightSide)
                        matchingPairs = matchingPairs + newPair
                        leftSide = ""
                        rightSide = ""
                    }
                },
                enabled = leftSide.isNotEmpty() && rightSide.isNotEmpty()
            ) {
                Text("Add Pair")
            }
        }

        matchingPairs.forEach { pair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("${pair.first} -> ${pair.second}", modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { matchingPairs = matchingPairs - pair }
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Pair")
                }
            }
        }
    }
}