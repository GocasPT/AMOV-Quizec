package pt.isec.amov.quizec.ui.screens.question.create

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
import pt.isec.amov.quizec.model.question.Answer.Drag

@Composable
fun DragQuestion(
    onAnswerChanged: (Answer) -> Unit,
    saveEnabled: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    questionTitle : String
) {
    var selectedWords by remember { mutableStateOf<Set<Pair<Int, String>>>(emptySet()) }
    val scrollState = rememberScrollState()

    LaunchedEffect(selectedWords) {
        onAnswerChanged(Drag(selectedWords))
        saveEnabled(selectedWords.isNotEmpty())
    }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Select the words")
                }
                append(" to scramble")
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