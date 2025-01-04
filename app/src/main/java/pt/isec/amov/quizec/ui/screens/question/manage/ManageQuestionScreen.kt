package pt.isec.amov.quizec.ui.screens.question.manage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Answer.Drag
import pt.isec.amov.quizec.model.question.Answer.FillBlank
import pt.isec.amov.quizec.model.question.Answer.Matching
import pt.isec.amov.quizec.model.question.Answer.MultipleChoice
import pt.isec.amov.quizec.model.question.Answer.Ordering
import pt.isec.amov.quizec.model.question.Answer.SingleChoice
import pt.isec.amov.quizec.model.question.Answer.TrueFalse
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionType

val questionTest3 =  Question(
    id = 2,
    content = "Quantos anos asdfdfh ah sah dashd ashd hsad sahd sahd ahd hsa dhas dhas d tem o Buno?",
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

val questionTest2 = Question(
    id = 2,
    content = "Quantos anos asdfdfh ah sah dashd ashd hsad sahd sahd ahd hsa dhas dhas d tem o Buno?",
    image = "Image URL",
    answers = Answer.MultipleChoice(
        setOf(
            Pair(true, "20"),
            Pair(false, "21"),
            Pair(false, "22"),
//            Pair(true, "23"),
//            Pair(false, "25"),
//            Pair(false, "36"),
//            Pair(true, "64"),
//            Pair(false, "64"),
//            Pair(false, "53"),
        )
    ),
    user = "User"
)



@Preview(showBackground = true)
@Composable
fun ManageQuestionScreen(
    question: Question? = questionTest2,
    userId : String = "",
    saveQuestion: (Question) -> Unit = {}
) {
    var questionContent by remember { mutableStateOf(question?.content ?: "") }
    var questionAnswers by remember { mutableStateOf(question?.answers ?: TrueFalse(false)) }

    var isExpanded by remember { mutableStateOf(false) }
    var saveEnabled by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Get Back",
                    tint = Color.Gray
                )
            }
        }

        Text(
            text = if (question == null) "New Question" else "Edit Question",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = questionContent,
            onValueChange = { questionContent = it },
            label = { Text("Question") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        QuestionTypeDropdown(
            currentAnswer = questionAnswers,
            isExpanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            onAnswerSelected = {
                questionAnswers = when (it) {
                    QuestionType.TRUE_FALSE -> TrueFalse(false)
                    QuestionType.SINGLE_CHOICE -> SingleChoice(emptySet())
                    QuestionType.MULTIPLE_CHOICE -> MultipleChoice(emptySet())
                    QuestionType.MATCHING -> Matching(emptySet())
                    QuestionType.ORDERING -> Ordering(emptyList())
                    QuestionType.DRAG -> Drag(emptySet())
                    //P07 - Not implemented
                    QuestionType.FILL_BLANK -> FillBlank(emptySet())
                }
            }
        )
        
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
        ) {
            when (questionAnswers) {
                is TrueFalse -> YesNoQuestion(
                    initialAnswer = questionAnswers as TrueFalse,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it }
                )

                is SingleChoice -> SingleChoiceQuestion(
                    initialAnswer = questionAnswers as SingleChoice,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState
                )

                is MultipleChoice -> MultipleChoiceQuestion(
                    initialAnswer = questionAnswers as MultipleChoice,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState
                )

                is Matching -> MatchingQuestion(
                    initialAnswer = questionAnswers as Matching,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState
                )

                is Ordering -> OrderingQuestion(
                    initialAnswer = questionAnswers as Ordering,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState
                )

                is Drag -> DragQuestion(
                    initialAnswer = questionAnswers as Drag,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    modifier = Modifier.weight(1f),
                    questionTitle = questionContent
                )

                is FillBlank -> FillBlankQuestion(
                    initialAnswer = questionAnswers as FillBlank,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    modifier = Modifier.weight(1f),
                    questionTitle = questionContent
                )
            }
        }

        Button(
            onClick = {
                saveQuestion(
                    question?.copy(
                        content = questionContent,
                        answers = questionAnswers
                    ) ?: Question(
                        id = null,
                        image = null,
                        content = questionContent,
                        answers = questionAnswers,
                        user = userId
                    )
                )
            },
            modifier = Modifier.padding(top = 16.dp),
            enabled = saveEnabled && questionContent.isNotBlank()
        ) {
            Text(if (question == null) "Create Question" else "Save Changes")
        }

        Button(
            onClick = {}
        ) {
            Text("Duplicate Question")
        }
    }
}