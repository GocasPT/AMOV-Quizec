package pt.isec.amov.quizec.ui.screens.question.manage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionCounter
import pt.isec.amov.quizec.model.question.QuestionType

@Composable
fun ManageQuestionScreen(
    question: Question?,
    saveQuestion: (Question) -> Unit
) {
    var questionContent by remember { mutableStateOf(question?.content ?: "") }
    var questionType by remember { mutableStateOf(question?.type ?: QuestionType.YES_NO) }
    var questionAnswers by remember { mutableStateOf(question?.answers?.firstOrNull() ?: Answer.TrueFalse(false)) }

    var isExpanded by remember { mutableStateOf(false) }
    var saveEnabled by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (question == null) "New Question" else "Edit Question",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = questionContent,
            onValueChange = { questionContent = it },
            label = { Text("Question Text") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        QuestionTypeDropdown(
            questionType = questionType,
            isExpanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            onTypeSelected = { selectedType ->
                questionType = selectedType

                questionAnswers = when (selectedType) {
                    QuestionType.YES_NO -> Answer.TrueFalse(false)
                    QuestionType.SINGLE_CHOICE -> Answer.SingleChoice(setOf(), "")
                    QuestionType.MULTIPLE_CHOICE -> Answer.MultipleChoice(setOf(), setOf())
                    QuestionType.MATCHING -> Answer.Matching(setOf())
                    QuestionType.ORDERING -> Answer.Ordering(listOf())
                    QuestionType.DRAG -> Answer.Drag(setOf())
                    QuestionType.FILL_BLANK -> Answer.FillBlank(setOf())
                }
            }
        )

        when (questionType) {
            QuestionType.YES_NO -> YesNoQuestion(
                initialAnswer = questionAnswers as Answer.TrueFalse,
                onAnswerChanged = { questionAnswers = it },
                saveEnabled = { saveEnabled = it }
            )
            QuestionType.SINGLE_CHOICE -> SingleChoiceQuestion(
                initialAnswer = questionAnswers as Answer.SingleChoice,
                onAnswerChanged = { questionAnswers = it },
                saveEnabled = { saveEnabled = it },
                modifier = Modifier.weight(1f),
                scrollState = scrollState
            )
            QuestionType.MULTIPLE_CHOICE -> MultipleChoiceQuestion(
                initialAnswer = questionAnswers as Answer.MultipleChoice,
                onAnswerChanged = { questionAnswers = it },
                saveEnabled = { saveEnabled = it },
                modifier = Modifier.weight(1f),
                scrollState = scrollState
            )
            QuestionType.MATCHING -> MatchingQuestion(
                initialAnswer = questionAnswers as Answer.Matching,
                onAnswerChanged = { questionAnswers = it },
                saveEnabled = { saveEnabled = it },
                modifier = Modifier.weight(1f),
                scrollState = scrollState
            )
            QuestionType.ORDERING -> OrderingQuestion(
                initialAnswer = questionAnswers as Answer.Ordering,
                onAnswerChanged = { questionAnswers = it },
                saveEnabled = { saveEnabled = it },
                modifier = Modifier.weight(1f),
                scrollState = scrollState
            )
            QuestionType.DRAG -> DragQuestion(
                initialAnswer = questionAnswers as Answer.Drag,
                onAnswerChanged = { questionAnswers = it },
                saveEnabled = { saveEnabled = it },
                modifier = Modifier.weight(1f),
                questionTitle = questionContent
            )
            QuestionType.FILL_BLANK -> FillBlankQuestion(
                initialAnswer = questionAnswers as Answer.FillBlank,
                onAnswerChanged = { questionAnswers = it },
                saveEnabled = { saveEnabled = it },
                modifier = Modifier.weight(1f),
                questionTitle = questionContent
            )
        }

        Button(
            onClick = {
                saveQuestion(
                    question?.copy(
                        content = questionContent,
                        type = questionType,
                        answers = listOf(questionAnswers)
                    ) ?: Question(
                        id = QuestionCounter.getNextId(),
                        image = null,
                        content = questionContent,
                        type = questionType,
                        answers = listOf(questionAnswers)
                    )
                )
            },
            modifier = Modifier.padding(top = 16.dp),
            enabled = saveEnabled && questionContent.isNotBlank()
        ) {
            Text(if (question == null) "Create Question" else "Save Changes")
        }
    }
}