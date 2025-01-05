package pt.isec.amov.quizec.ui.screens.question.manage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer

@Composable
fun YesNoQuestion(
    initialAnswer: Answer.TrueFalse,
    onAnswerChanged: (Answer) -> Unit,
    saveEnabled: (Boolean) -> Unit
) {
    var trueFalseAnswer by remember { mutableStateOf(initialAnswer.rightAnswer) }

    LaunchedEffect(trueFalseAnswer) {
        onAnswerChanged(Answer.TrueFalse(trueFalseAnswer))
        saveEnabled(true)
    }

    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.false_option))
        Switch(
            checked = trueFalseAnswer,
            onCheckedChange = { trueFalseAnswer = it }
        )
        Text(stringResource(R.string.true_option))
    }
}

@Composable
fun YesNoQuestionDisplay(
    selectedOption: Boolean,
    onOptionSelected: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.false_option))
        Switch(
            checked = selectedOption,
            onCheckedChange = { onOptionSelected(it) }
        )
        Text(stringResource(R.string.true_option))
    }
}