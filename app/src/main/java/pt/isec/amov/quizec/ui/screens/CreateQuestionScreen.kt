package pt.isec.amov.quizec.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            OutlinedTextField(
                value = questionType.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Question Type") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        Modifier.clickable { isExpanded = !isExpanded }
                    )
                },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                QuestionType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            questionType = type
                            isExpanded = false
                        }
                    )
                }
            }
        }

        when (questionType) {
            QuestionType.YES_NO -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("True")
                    Switch(
                        checked = (questionAnswers as TrueFalse).answer,
                        onCheckedChange = { questionAnswers = TrueFalse(it) }
                    )
                    Text("False")
                }
            }
            else -> {

            }
        }

        Button(
            onClick = {
                saveQuestion(Question(questionTitle, questionType, null, listOf(questionAnswers)))
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save question")
        }
    }
}