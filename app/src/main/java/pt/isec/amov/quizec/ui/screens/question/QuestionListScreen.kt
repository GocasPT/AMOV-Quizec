package pt.isec.amov.quizec.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
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
import pt.isec.amov.quizec.ui.screens.quiz.QuizCardV2


val questionTest = listOf(
    Question(
        id = 1,
        content = "Question 1",
        image = "Image URL",
        answers = Answer.TrueFalse(true),
        user = "User"
    ),
    Question(
        id = 2,
        content = "Question 2 asujibdsau dsa dsadubsnaoid sapdjsadnhsaldn as'daisjdoasijd sapodj saiodjasiodjsadj",
        image = "Image URL",
        answers = Answer.MultipleChoice(
            setOf(
                Pair(true, "Answer 1"),  // Correct answer
                Pair(false, "Answer 2"), // Incorrect answer
                Pair(false, "Answer 3")  // Incorrect answer
            )
        ),
        user = "User"
    ),
)
@Preview(showBackground = true)
@Composable
fun QuestionListScreen(
    questionList: List<Question> = questionTest,
    onSelectQuestion: (Question) -> Unit = {},
    onCreateQuestion: () -> Unit = {},
    onEditQuestion: (Question) -> Unit = {},
    onDeleteQuestion: (Question) -> Unit = {},
    onDuplicateQuestion: (Question) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filterOptions = listOf("True / False", "Single Choice")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomList(
                items = questionList,
                onSelectItem = { question -> onSelectQuestion(question as Question) }) { question, onSelect ->
                QuestionCardV2(
                    question = question as Question,
                    onSelectQuestion = onSelectQuestion,
                    onEditQuestion = onEditQuestion,
                    onDeleteQuestion = onDeleteQuestion,
                    onDuplicateQuestion = onDuplicateQuestion,
                )
            }
        }

        ExtendedFloatingActionButton (
            onClick = {
                onCreateQuestion()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp)
        ) {
            Icon(
                modifier = Modifier
                    .padding(2.dp),
                contentDescription = "Create Question",
                imageVector = Icons.Filled.Add
            )
            Text("CREATE QUESTION")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionCard(
    question: Question,
    onSelectQuestion: (Question) -> Unit,
    onEditQuestion: (Question) -> Unit,
    onDeleteQuestion: (Question) -> Unit,
    onDuplicateQuestion : (Question) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .combinedClickable(
                onClick = { onSelectQuestion(question) },
                onLongClick = { expanded = true }
            ),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(192, 255, 255)
        )) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                question.image?.let {
                    //TODO: get image from string and "return" image/file
                    //PLACE_HOLDER
                    Icon(Icons.Filled.AccountCircle, "Question image")
                    Spacer(modifier = Modifier.padding(8.dp))
                }
                Text(
                    text = question.content, fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Type: ${question.answers.answerType.displayName}", fontSize = 16.sp
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Duplicate") },
                onClick = {
                    expanded = false
                    onDuplicateQuestion(question)
                }
            )
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = {
                    expanded = false
                    onEditQuestion(question)
                }
            )
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    expanded = false
                    onDeleteQuestion(question)
                }
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionCardV2(
    question: Question,
    onSelectQuestion: (Question) -> Unit,
    onEditQuestion: (Question) -> Unit,
    onDeleteQuestion: (Question) -> Unit,
    onDuplicateQuestion : (Question) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .combinedClickable(
                onClick = { onSelectQuestion(question) },
                onLongClick = { expanded = true }
            ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Box(
            modifier = Modifier
                .height(100.dp)
        ) {
            question.image?.let {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Quiz Image",
                    contentScale = ContentScale.Crop
                )
            }
                ?: Image (
                modifier = Modifier
                    .fillMaxSize(),
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
                    .padding(6.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopStart),
                    text = question.content,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                AssistChip(
                    border = BorderStroke(2.dp, Color.White),
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    onClick = { /*TODO*/ },
                    label = { Text(question.answers.answerType.displayName, color = Color.White) },
                    leadingIcon = {
                        Icon(
                            tint = Color.White,
                            imageVector = question.answers.answerType.icon,
                            contentDescription = question.answers.answerType.displayName,
                            modifier = Modifier
                                .size(AssistChipDefaults.IconSize)
                        )
                    }
                )
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
                    onDuplicateQuestion(question)
                }
            )
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = {
                    expanded = false
                    onEditQuestion(question)
                }
            )
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    expanded = false
                    onDeleteQuestion(question)
                }
            )
        }
    }
}