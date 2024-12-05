package pt.isec.amov.quizec.ui.screens.question.manage

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.question.Answer

@Composable
fun FillBlankQuestion(
    initialAnswer: Answer.FillBlank,
    onAnswerChanged: (Answer) -> Unit,
    saveEnabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    questionTitle : String
) {
    var selectedWords by remember { mutableStateOf(initialAnswer.answers) }
    val scrollState = rememberScrollState()

    LaunchedEffect(questionTitle) {
        val newWords = questionTitle.split(" ").mapIndexed { index, word -> index to word }
        selectedWords = selectedWords.filter { (_, word) -> newWords.any { it.second == word } }.toSet()
    }

    LaunchedEffect(selectedWords) {
        onAnswerChanged(Answer.FillBlank(selectedWords))
        saveEnabled(selectedWords.isNotEmpty())
    }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Fill in the blanks")
                }
                append(" by selecting the words from the text above")
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
            text = "Question Text Preview: ${questionTitle.split(" ").mapIndexed { index, word ->
                if (selectedWords.contains(index to word)) "____" else word
            }.joinToString(" ")}"
        )
    }
}