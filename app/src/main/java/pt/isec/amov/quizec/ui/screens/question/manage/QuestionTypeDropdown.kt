package pt.isec.amov.quizec.ui.screens.question.manage

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.QuestionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionTypeDropdown(
    currentAnswer: Answer,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onAnswerSelected: (QuestionType) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            value = currentAnswer.type.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Question Type") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                    Modifier.clickable { onExpandedChange(!isExpanded) }
                )
            },
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            QuestionType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.displayName) },
                    onClick = {
                        onExpandedChange(false)
                        onAnswerSelected(type)
                    }
                )
            }
        }
    }
}