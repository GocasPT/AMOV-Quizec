package pt.isec.amov.quizec.ui.screens.quiz.live

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.ui.screens.question.manage.MultipleChoiceDisplay
import pt.isec.amov.quizec.ui.screens.question.manage.SingleChoiceDisplay
import pt.isec.amov.quizec.ui.screens.question.manage.YesNoQuestionDisplay

@Composable
fun QuizLiveScreen(
    question: Question,
    onAnswerSelected: (String) -> Unit = {},
    onNextQuestion: () -> Unit = {}
) {
    var selectedAnswer by remember {
        mutableStateOf<String?>(
            if (question.answers is Answer.TrueFalse) "false" else null
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Get Back",
                        tint = Color(0xFFD70202)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        text = "00:00",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            //TODO: isto tem q rodar
            Column(
                modifier = Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        text = question.content,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    //        question.image?.let {
//            Image(
//                painter = rememberImagePainter(question.image),
//                contentDescription = "Question Image",
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        CardQuestionInfo(
                            question = question,
                            defaultValue = selectedAnswer,
                            onResponse = { selectedAnswer = it }
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomCenter),
        ) {
            Button(
                onClick = {
                    selectedAnswer?.let {
                        onAnswerSelected(it)
                        onNextQuestion()
                    }
                },
                enabled = selectedAnswer != null
            ) {
                Text(text = stringResource(R.string.next_question))
            }
        }
    }
}

@Composable
fun CardQuestionInfo(
    question: Question,
    defaultValue: String? = null,
    onResponse: (String) -> Unit = {}
){
    var selectedOption by remember { mutableStateOf(defaultValue?.toBoolean() ?: false) }
    var selectedAnswer by remember { mutableStateOf<Pair<Boolean, String>>(Pair(false, "False")) }
    var selectedAnswers by remember { mutableStateOf<Set<Pair<Boolean, String>>>(emptySet()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        when (question.answers) {
            is Answer.TrueFalse -> {
                YesNoQuestionDisplay(
                    selectedOption = selectedOption,
                    onOptionSelected = {
                        selectedOption = it
                        onResponse(it.toString())
                    }
                )

                LaunchedEffect(Unit) {
                    onResponse(selectedOption.toString())
                }
            }

            is Answer.SingleChoice -> {
                SingleChoiceDisplay(
                    answers = question.answers.answers,
                    selectedOption = selectedAnswer,
                    onOptionSelected = { selectedAnswer = it }
                )
            }

            is Answer.MultipleChoice -> {
                MultipleChoiceDisplay(
                    answers = question.answers.answers,
                    selectedOptions = selectedAnswers,
                    onOptionsSelected = {
                        selectedAnswers = if (selectedAnswers.contains(it)) {
                            selectedAnswers - it
                        } else {
                            selectedAnswers + it
                        }
                    }
                )
            }


            is Answer.Drag -> TODO()
            is Answer.FillBlank -> TODO()
            is Answer.Matching -> TODO()
            is Answer.Ordering -> TODO()
        }

//        when(question.answers.answerType) {
//            QuestionType.TRUE_FALSE -> YesNoQuestionDisplay(
//                selectedOption = selectedOption,
//                onOptionSelected = { selectedOption = it }
//            )
//
//            QuestionType.SINGLE_CHOICE -> SingleChoiceDisplay(
//                answers = question.answers as Set<Pair<Boolean, String>>,
//                selectedOption = selectedAnswer,
//                onOptionSelected = { selectedAnswer = it }
//            )
//
//            QuestionType.MULTIPLE_CHOICE -> MultipleChoiceDisplay(
//                answers = question.answers as Set<Pair<Boolean, String>>,
//                selectedOptions = selectedAnswers,
//                onOptionSelected = { selectedAnswers = it }
//            )
//
//            QuestionType.MATCHING -> TODO()
//            QuestionType.ORDERING -> TODO()
//            QuestionType.DRAG -> TODO()
//            QuestionType.FILL_BLANK -> TODO()
//        }
    }

}

@Preview(showBackground = true)
@Composable
fun QuizLiveScreenPreview() {
    QuizLiveScreen(
        question = Question(
            id = 1,
            content = "Quantos anos tem o Buno?",
            image = "Image URL",
            answers = Answer.SingleChoice(
                setOf(
                    Pair(true, "20"),
                    Pair(false, "21"),
                    Pair(false, "22"),
                )
            ),
            user = "User"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CardQuestionInfoPreview_TrueFalse() {
    CardQuestionInfo(
        question = Question(
            id = 2,
            content = "Quantos anos tem o Buno?",
            image = "Image URL",
            answers = Answer.TrueFalse(true),
            user = "User"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CardQuestionInfoPreview_SingleChoice() {
    CardQuestionInfo(
        question = Question(
            id = 2,
            content = "Quantos anos tem o Buno?",
            image = null,
            answers = Answer.SingleChoice(
                setOf(
                    Pair(true, "20"),
                    Pair(false, "21"),
                    Pair(false, "22"),
                )
            ),
            user = "User"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CardQuestionInfoPreview_MultipleChoice() {
    CardQuestionInfo(
        question = Question(
            id = 2,
            content = "Quantos anos tem o Buno?",
            image = "Image URL",
            answers = Answer.MultipleChoice(
                setOf(
                    Pair(true, "20"),
                    Pair(false, "21"),
                    Pair(false, "22"),
                )
            ),
            user = "User"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CardQuestionInfoPreview_Matching() {
    CardQuestionInfo(
        question = Question(
            id = 2,
            content = "Quantos anos tem o Buno?",
            image = "Image URL",
            answers = Answer.Matching(
                setOf(
                    Pair("1", "20"),
                    Pair("2", "21"),
                    Pair("3", "22"),
                )
            ),
            user = "User"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CardQuestionInfoPreview_Ordering() {
    CardQuestionInfo(
        question = Question(
            id = 2,
            content = "Quantos anos tem o Buno?",
            image = "Image URL",
            answers = Answer.Ordering(
                listOf(
                    "20",
                    "21",
                    "22"
                )
            ),
            user = "User"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CardQuestionInfoPreview_Drag() {
    CardQuestionInfo(
        question = Question(
            id = 2,
            content = "Quantos anos tem o Buno?",
            image = "Image URL",
            answers = Answer.Drag(
                setOf(
                    Pair(1, "20"),
                    Pair(2, "21"),
                    Pair(3, "22"),
                )
            ),
            user = "User"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CardQuestionInfoPreview_FillBlank() {
    CardQuestionInfo(
        question = Question(
            id = 2,
            content = "Quantos anos tem o Buno?",
            image = "Image URL",
            answers = Answer.FillBlank(
                setOf(
                    Pair(1, "20"),
                    Pair(2, "21"),
                    Pair(3, "22"),
                )
            ),
            user = "User"
        )
    )
}