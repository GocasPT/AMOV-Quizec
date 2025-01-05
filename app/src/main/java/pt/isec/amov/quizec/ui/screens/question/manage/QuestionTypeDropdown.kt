package pt.isec.amov.quizec.ui.screens.question.manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.R
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
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        expanded = isExpanded,
        onExpandedChange = onExpandedChange
    ) {
            OutlinedTextField(
                value = currentAnswer.answerType.displayName,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.question_type)) },
                trailingIcon = {
                    IconButton(
                        onClick = { onExpandedChange(!isExpanded) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Icon Dropdown",
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
            )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            QuestionType.entries.forEach { type ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){

                            Icon(
                                imageVector = type.icon,
                                contentDescription = type.toString(),
                                modifier = Modifier.size(20.dp) // Adjust size as needed
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(type.displayName)
                        }

                    },
                    onClick = {
                        onExpandedChange(false)
                        onAnswerSelected(type)
                    }
                )
            }
        }
    }
}