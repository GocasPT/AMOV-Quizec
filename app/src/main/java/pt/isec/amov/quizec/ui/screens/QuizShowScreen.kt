package pt.isec.amov.quizec.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.Quiz

@Composable
fun QuizShowScreen(
    quiz: Quiz,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = quiz.title,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        QuestionListScreen(
            questionList = quiz.questions,
            onSelectQuestion = {
                //TODO
            }
        )
    }
}