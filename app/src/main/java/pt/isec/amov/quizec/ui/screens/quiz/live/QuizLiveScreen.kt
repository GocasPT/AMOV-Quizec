package pt.isec.amov.quizec.ui.screens.quiz.live

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Question


val questionTestvv =  Question(
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
fun QuizLiveScreen(
    question: Question = questionTestvv,
    onAnswerSelected: (String) -> Unit = {},
    onNextQuestion: () -> Unit = {}
) {
    
    var selectedAnswer by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Get Back",
                        tint = Color(0xFFD70202)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        text = "00:00",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        text = "a questao é a seguintedasdsad asd asdasdsad ads a?",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    //        question.image?.let {
//            Image(
//                painter = rememberImagePainter(question.image),
//                contentDescription = "Question Image",
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "Question")
//                (question.answers as? Answer.SingleChoice)?.let { answer ->
//                    answer. { option ->
//                        Button(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 8.dp),
//                            onClick = {
//                                selectedAnswer = option.second
//                            },
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = if (selectedAnswer == option.second) Color.LightGray else Color.Gray
//                            )
//                        ) {
//                            Text(text = option.second)
//                        }
//                    }
//                }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomCenter),
        ) {
            Button(
                onClick = {
                    selectedAnswer?.let {
                        onAnswerSelected(it)
                        onNextQuestion()
                    }
                }
            ) {
                Text(text = "Next Question")
            }
        }
    }
}
