package pt.isec.amov.quizec.ui.screens.question.manage

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Answer.FillBlank
import pt.isec.amov.quizec.model.question.Question

@Composable
fun FillBlankQuestion(
    initialAnswer: FillBlank,
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
        onAnswerChanged(FillBlank(selectedWords))
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
                    append(stringResource(R.string.fill_in_the_blanks))
                }
                append(stringResource(R.string.by_selecting_the_words_from_the_text_above))
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
                    ),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = word,
                        color = if (selectedWords.contains(index to word)) Color.White else Color.Black
                    )
                }
            }
        }

        Text(
            text = stringResource(
                R.string.question_text_preview,
                questionTitle.split(" ").mapIndexed { index, word ->
                    if (selectedWords.contains(index to word)) "_".repeat(word.length) else word
                }.joinToString(" ")
            )
        )
    }
}

@Composable
fun FillBlankQuestionDisplay(
    question: Question,
    answersSelected: Set<Pair<Int, String>>,
    onAnswersSelectedChange: (Set<Pair<Int, String>>) -> Unit
) {

    val blankOptions = (question.answers as? FillBlank)?.answers ?: emptySet()
    var blankInputs by remember { mutableStateOf(blankOptions.associate { it.first to "" }) }

    val questionContentWithoutResponse = question.content
        .split(" ")
        .mapIndexed { index, word ->
            if (blankOptions.any { it.first == index }) "_".repeat(word.length)
            else word
        }.joinToString(" ")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = questionContentWithoutResponse
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            blankOptions.forEachIndexed { index, (blankIndex, blankWord) ->
                TextField(
                    value = blankInputs[blankIndex] ?: "",
                    onValueChange = { newValue ->
                        if (newValue.length <= blankWord.length) {
                            blankInputs = blankInputs.toMutableMap().apply {
                                this[blankIndex] = newValue
                            }
                            val updatedAnswers = blankInputs.filter { it.value.isNotEmpty() }
                                .map { Pair(it.key, it.value) }
                                .toSet()
                            onAnswersSelectedChange(updatedAnswers)
                        }
                    },
                    label = { Text(
                        stringResource(R.string.hint)
                                + blankWord.length
                                + stringResource(R.string.letters))
                            },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}