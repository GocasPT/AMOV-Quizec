package pt.isec.amov.quizec.ui.screens.question.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Answer.TrueFalse
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestionScreen(
    saveQuestion: (Question) -> Unit
) {
    var questionTitle by remember { mutableStateOf("") }
    var questionType by remember { mutableStateOf<QuestionType>(QuestionType.YES_NO) }
    var questionAnswers by remember { mutableStateOf<Answer>(TrueFalse(false)) }
    var isExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var saveEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "New Question",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = questionTitle,
            onValueChange = { questionTitle = it },
            label = { Text("Question Text") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        QuestionTypeDropdown(
            questionType = questionType,
            isExpanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            onTypeSelected = { questionType = it },
        )

        when (questionType) {
            QuestionType.YES_NO -> YesNoQuestion(
                onAnswerChanged = { questionAnswers = it },
                saveEnabled = { saveEnabled = it }
            )
            QuestionType.SINGLE_CHOICE -> SingleChoiceQuestion(
                onAnswerChanged = { questionAnswers = it },
                saveEnabled = { saveEnabled = it },
                modifier = Modifier.weight(1f),
                scrollState = scrollState
            )
            QuestionType.MULTIPLE_CHOICE -> MultipleChoiceQuestion(
                onAnswerChanged = { questionAnswers = it },
                saveEnabled = { saveEnabled = it },
                modifier = Modifier.weight(1f),
                scrollState = scrollState
            )
            QuestionType.MATCHING -> MatchingQuestion(
                onAnswerChanged = { questionAnswers = it },
                saveEnabled = { saveEnabled = it },
                modifier = Modifier.weight(1f),
                scrollState = scrollState
            )
            QuestionType.ORDERING -> OrderingQuestion(
                onAnswerChanged = { questionAnswers = it },
                saveEnabled = { saveEnabled = it },
                modifier = Modifier.weight(1f),
                scrollState = scrollState
            )
            else -> {
                // TODO: Handle other question types
            }
        }

        Button(
            onClick = {
                saveQuestion(Question(questionTitle, questionType, null, listOf(questionAnswers)))
            },
            modifier = Modifier.padding(top = 16.dp),
            enabled = saveEnabled && questionTitle.isNotBlank()
        ) {
            Text("Save question")
        }
    }
}