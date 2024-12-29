package pt.isec.amov.quizec.ui.screens.question

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import pt.isec.amov.quizec.model.question.Question

@Composable
fun QuestionShowScreen(
    question: Question,
    modifier: Modifier = Modifier,
) {
    var imageURL by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
    ) {
        Text(
            text = question.content,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        AsyncImage(
            model = imageURL,
            contentDescription = "Contact's picture",
            modifier = Modifier
                .fillMaxSize(0.5f)
                .aspectRatio(1f)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally),
        )

    }
}