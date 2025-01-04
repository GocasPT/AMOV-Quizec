package pt.isec.amov.quizec.ui.screens.question

import android.widget.TextView.OnEditorActionListener
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.ui.screens.question.manage.DragQuestion
import pt.isec.amov.quizec.ui.screens.question.manage.FillBlankQuestion


val questionTest =  Question(
    id = 2,
    content = "Quantos anos tem o Buno?",
    image = "Image URL",
    answers = Answer.SingleChoice(
        setOf(
            Pair(true, "20"),
            Pair(false, "21"),
            Pair(false, "22"),
        )
    ),
    user = "User"
)

@Preview(showBackground = true)
@Composable
fun QuestionShowScreen(
    question: Question = questionTest,
    modifier: Modifier = Modifier,
    onBack : () -> Unit = {},
    onEdit : (Question) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = {
                    onBack()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Go Back to Previous Screen",
                    tint = Color.Gray
                )
            }

            IconButton(
                onClick = {
                    onEdit(question)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Question",
                    tint = Color.Gray
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "ID: ${question.id ?: "N/A"}",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = question.content,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(171, 228, 161, 255),
            ),
        ) {
            DragQuestion(
                initialAnswer = Answer.Drag(setOf()),
                onAnswerChanged = {},
                saveEnabled = {},
                questionTitle = question.content
            )
//            FillBlankQuestion(
//                initialAnswer = Answer.FillBlank("test", "test2"),
//                onAnswerChanged = {},
//                saveEnabled = {},
//                questionTitle = question.content
//            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        //TODO: Show all details
    }
}