package pt.isec.amov.quizec.ui.screens.question.manage

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("False")
        Switch(
            checked = trueFalseAnswer,
            onCheckedChange = { trueFalseAnswer = it }
        )
        Text("True")
    }
}