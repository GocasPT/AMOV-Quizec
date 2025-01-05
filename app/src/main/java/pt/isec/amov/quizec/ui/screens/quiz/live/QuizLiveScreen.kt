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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.ui.screens.question.manage.FillBlankQuestionDisplay
import pt.isec.amov.quizec.ui.screens.question.manage.MatchingQuestionDisplay
import pt.isec.amov.quizec.ui.screens.question.manage.MultipleChoiceDisplay
import pt.isec.amov.quizec.ui.screens.question.manage.OrderingQuestionDisplay
import pt.isec.amov.quizec.ui.screens.question.manage.SingleChoiceDisplay
import pt.isec.amov.quizec.ui.screens.question.manage.YesNoQuestionDisplay

@Composable
fun QuizLiveScreen(
    question: Question,
    onAnswerSelected: (Answer) -> Unit = {},
    onNextQuestion: () -> Unit = {}
) {
    var selectedAnswer by remember { mutableStateOf<Answer>(Answer.TrueFalse(false)) }

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
                            defaultValue = selectedAnswer.toString(),
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
                    selectedAnswer.let {
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
    onResponse: (Answer) -> Unit = {}
) {
    var selectedOption by remember { mutableStateOf(defaultValue?.toBoolean() ?: false) }
    var selectedAnswer by remember { mutableStateOf<Pair<Boolean, String>>(Pair(false, "False")) }
    var selectedAnswers by remember { mutableStateOf<Set<Pair<Boolean, String>>>(emptySet()) }
    var selectedFilledAnswers by remember { mutableStateOf(setOf<Pair<Int, String>>()) }
    var selectedOrder by remember { mutableStateOf(listOf<String>()) }

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

                        //onResponse(selectedOption)
                        onResponse(Answer.TrueFalse(selectedOption))
                    }
                )

                //?TODO: check this
                /*LaunchedEffect(Unit) {
                    onResponse(selectedOption)
                }*/
            }

            is Answer.SingleChoice -> {
                SingleChoiceDisplay(
                    answers = question.answers.answers,
                    selectedOption = selectedAnswer,
                    onOptionSelected = {
                        selectedAnswer = it
                        //onResponse(selectedAnswer)
                        onResponse(
                            Answer.SingleChoice(question.answers.answers.map {
                                if (it.second == selectedAnswer.second)
                                    return@map Pair(true, it.second)
                                else
                                    return@map Pair(false, it.second)
                            }.toSet())
                        )
                    }
                )
            }

            is Answer.MultipleChoice -> {
                MultipleChoiceDisplay(
                    answers = question.answers.answers,
                    selectedOptions = selectedAnswers,
                    onOptionSelected = {
                        selectedAnswers = if (selectedAnswers.contains(it)) {
                            selectedAnswers - it
                        } else {
                            selectedAnswers + it
                        }

                        //onResponse(selectedAnswers)
                        onResponse(
                            Answer.MultipleChoice(question.answers.answers.map {
                                if (selectedAnswers.contains(it))
                                    return@map Pair(true, it.second)
                                else
                                    return@map Pair(false, it.second)
                            }.toSet())
                        )
                    }
                )
            }

            is Answer.Matching -> {
                MatchingQuestionDisplay()
            }

            is Answer.Ordering -> {
                OrderingQuestionDisplay(
                    order = question.answers.order,
                    onOrderChange = { updatedOrder ->
                        selectedOrder = updatedOrder

                        //onResponse(selectedOrder)
                        onResponse(
                            Answer.Ordering(selectedOrder)
                        )

                    }

                )
            }

            is Answer.Drag -> {

            }

            is Answer.FillBlank -> {
                FillBlankQuestionDisplay(
                    question = question,
                    answersSelected = selectedFilledAnswers,
                    onAnswersSelectedChange = { updatedAnswers ->
                        selectedFilledAnswers = updatedAnswers

                        //onResponse(selectedFilledAnswers)
                        onResponse(
                            Answer.FillBlank(question.answers.answers.map {
                                if (selectedFilledAnswers.contains(it))
                                    return@map Pair(it.first, it.second)
                                else
                                    return@map Pair(it.first, "")
                            }.toSet())
                        )
                    }
                )
            }
        }
    }

}