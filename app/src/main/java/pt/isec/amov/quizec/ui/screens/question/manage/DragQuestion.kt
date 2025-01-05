package pt.isec.amov.quizec.ui.screens.question.manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Answer.Drag

@Composable
fun DragQuestion(
    initialAnswer: Drag,
    onAnswerChanged: (Answer) -> Unit,
    saveEnabled: (Boolean) -> Unit,
    questionTitle: String
) {
    var selectedWords by remember { mutableStateOf(initialAnswer.answers) }
    val scrollState = rememberScrollState()

    LaunchedEffect(questionTitle) {
        val newWords = questionTitle.split(" ").mapIndexed { index, word -> index to word }
        selectedWords =
            selectedWords.filter { (_, word) -> newWords.any { it.second == word } }.toSet()
    }

    LaunchedEffect(selectedWords) {
        onAnswerChanged(Drag(selectedWords))
        saveEnabled(selectedWords.isNotEmpty())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(R.string.select_the_words))
                }
                append(stringResource(R.string.to_scramble))
            },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            questionTitle.split(" ").forEachIndexed { index, word ->
                if (word.isBlank()) return@forEachIndexed
                Button(
                    onClick = {
                        val wordPair = index to word
                        selectedWords = if (selectedWords.contains(wordPair)) {
                            selectedWords - wordPair
                        } else {
                            selectedWords + wordPair
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedWords.contains(index to word)) Color.Blue else Color.Gray
                    )
                ) {
                    Text(
                        text = word,
                        color = if (selectedWords.contains(index to word)) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DragQuestionDisplay(
    questionText: String,
    answers: Set<Pair<Int, String>>,
    selectedOrder: Set<Pair<Int, String>>,
    onOrderChanged: (Set<Pair<Int, String>>) -> Unit
) {
    var currentAnswers by remember { mutableStateOf(selectedOrder) }
    val shuffledAnswers = remember { answers.shuffled() } // Shuffle answers once and remember

    // Process text by replacing answer words with either the selected word or underscores
    val displayText = remember(currentAnswers) {
        var tempText = questionText
        answers.sortedByDescending { it.first }.forEach { (_, word) ->
            val replacement = if (currentAnswers.any { it.second == word }) word
            else "_".repeat(word.length)
            tempText = tempText.replace(word, replacement)
        }
        tempText
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Display the text with blanks/filled words
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Text(
                text = displayText,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Word options
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            shuffledAnswers.map { it.second }.forEach { word ->
                val isSelected = currentAnswers.any { it.second == word }
                Card(
                    modifier = Modifier
                        .clickable(
                            enabled = !isSelected,
                            onClick = {
                                // Find the index of the clicked word in the original answers
                                val originalIndex = answers.find { it.second == word }?.first

                                if (originalIndex != null) {
                                    // Check if there's an empty slot for this index in currentAnswers
                                    val emptySlot = currentAnswers.none { it.first == originalIndex }
                                    if (emptySlot) {
                                        // Place the selected word in the correct position
                                        val newAnswers = (currentAnswers + Pair(originalIndex, word)).toSet()
                                        currentAnswers = newAnswers
                                        onOrderChanged(newAnswers)
                                    }
                                }
                            }
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.surfaceVariant
                        else
                            MaterialTheme.colorScheme.surface
                    )
                ) {
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = word,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Button(
            modifier = Modifier.align(Alignment.End),
            onClick = {
                currentAnswers = emptySet()
                onOrderChanged(emptySet())
            }
        ) {
            Text(text = "Reset")
        }
    }
}