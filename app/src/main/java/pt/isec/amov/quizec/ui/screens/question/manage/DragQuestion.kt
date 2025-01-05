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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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