package pt.isec.amov.quizec.ui.screens.quiz

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.res.stringResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.ui.components.CustomList

@Composable
fun QuizListScreen(
    quizList: List<Quiz>,
    onSelectQuiz: (Quiz) -> Unit,
    onCreateQuiz: () -> Unit,
    onEditQuiz: (Quiz) -> Unit,
    onDeleteQuiz: (Quiz) -> Unit,
    onSearch: (String) -> Unit,
    onFilter: (String) -> Unit,
    onDuplicateQuiz: (Quiz) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.TopCenter),
//            contentAlignment = Alignment.Center
//        ) {
//            ExtendedFloatingActionButton (
//                onClick = {
//                    onCreateQuiz()
//                }
//            ) {
//                Icon(
//                    modifier = Modifier
//                        .padding(2.dp),
//                    contentDescription = "Create Quiz",
//                    imageVector = Icons.Filled.Add)
//                Text("CREATE QUIZ")
//            }
//        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomList(
                items = quizList,
                onSelectItem = { quiz -> onSelectQuiz(quiz as Quiz) }) { quiz, onSelect ->
                QuizCardV2(
                    quiz = quiz as Quiz,
                    onSelectQuiz = { onSelect(quiz) },
                    onEditQuiz = { onEditQuiz(quiz) },
                    onDeleteQuiz = { onDeleteQuiz(quiz) },
                    onDuplicateQuiz = { onDuplicateQuiz(quiz) }
                )
            }
        }

        ExtendedFloatingActionButton (
            onClick = {
                onCreateQuiz()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp)
        ) {
            Icon(
                modifier = Modifier
                    .padding(2.dp),
                contentDescription = "Create Quiz",
                imageVector = Icons.Filled.Add
            )
            Text(stringResource(R.string.create_quiz).uppercase())
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizCardV2(
    quiz: Quiz,
    onSelectQuiz: (Quiz) -> Unit,
    onEditQuiz: (Quiz) -> Unit,
    onDeleteQuiz: (Quiz) -> Unit,
    onDuplicateQuiz: (Quiz) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .combinedClickable(
                onClick = { onSelectQuiz(quiz) },
                onLongClick = { expanded = true }
            ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Box(
            modifier = Modifier
                .height(160.dp)
        ) {

            quiz.image?.let {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Quiz Image",
                    contentScale = ContentScale.Crop
                )
            } ?: Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.quizec_1080),
                contentDescription = "Quiz Image",
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Gray.copy(alpha = 0.8f)
                            ),
                            startY = 0f,
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = quiz.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.duplicate)) },
                onClick = {
                    expanded = false
                    onDuplicateQuiz(quiz)
                }
            )

            DropdownMenuItem(
                text = { Text(stringResource(R.string.edit)) },
                onClick = {
                    expanded = false
                    onEditQuiz(quiz)
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete)) },
                onClick = {
                    expanded = false
                    onDeleteQuiz(quiz)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizCard(
    quiz: Quiz,
    onSelectQuiz: (Quiz) -> Unit,
    onEditQuiz: (Quiz) -> Unit,
    onDeleteQuiz: (Quiz) -> Unit,
    onDuplicateQuiz: (Quiz) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .combinedClickable(
                onClick = { onSelectQuiz(quiz) },
                onLongClick = { expanded = true }
            ),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(255, 255, 192)
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                quiz.image?.let {
                    Icon(Icons.Filled.AccountCircle, it)
                    Spacer(modifier = Modifier.padding(8.dp))
                }
                Text(
                    text = quiz.title, fontSize = 20.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                /*
                quiz.maxTime?.let {
                    Text(text = "$it min", fontSize = 16.sp)
                    Spacer(modifier = Modifier.padding(2.dp))
                    Icon(Icons.Rounded.Info, "Max Time", Modifier.size(18.dp))
                }
                if (quiz.locationRestricted)
                    Icon(Icons.Filled.LocationOn, "Location Restricted", Modifier.size(18.dp))
                if (quiz.immediateResults)
                    Icon(Icons.Filled.Menu, "Immediate Results", Modifier.size(18.dp))
                 */
            }
            quiz.questions?.forEach { question ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Filled.PlayArrow, "Bullet Point", Modifier.size(18.dp))
                    Text(
                        text = "${question.content} - Type ${question.answers.answerType.displayName}",
                        fontSize = 16.sp
                    )
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Duplicate") },
                onClick = {
                    expanded = false
                    onDuplicateQuiz(quiz)
                }
            )
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = {
                    expanded = false
                    onEditQuiz(quiz)
                }
            )
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    expanded = false
                    onDeleteQuiz(quiz)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizListScreenPreview() {
    QuizListScreen(
        quizList = listOf(
            Quiz(
                null, "Quiz 1", null, "Sr. batata", listOf(
                    Question(null, "Question 1", null, Answer.TrueFalse(true), "Sr. batata"),
                    Question(null, "Question 2", null, Answer.SingleChoice(setOf()), "Sr. batata"),
                    Question(null, "Question 3", null, Answer.TrueFalse(true), "Sr. batata"),
                )
            ),
            Quiz(
                null, "Quiz 2", null, "Sr. batata", listOf(
                    Question(null, "Question 1", null, Answer.TrueFalse(true), "Sr. batata"),
                    Question(null, "Question 3", null, Answer.TrueFalse(true), "Sr. batata"),
                    Question(
                        null,
                        "Question 4",
                        null,
                        Answer.MultipleChoice(setOf()),
                        "Sr. batata"
                    ),
                )
            )
        ),
        onSelectQuiz = {},
        onCreateQuiz = {},
        onEditQuiz = {},
        onDeleteQuiz = {},
        onFilter = {},
        onSearch = {},
        onDuplicateQuiz = {}
    )
}