package pt.isec.amov.quizec.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.Question

@Composable
fun QuestionShowScreen(
    question: Question,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = question.title,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        //TODO: Show all details
    }
}