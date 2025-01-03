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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.ui.components.CustomList

val quizTeste = listOf(
    Quiz(
        id = 1,
        title = "Titulo 1",
        image = "Image URL",
        owner = "Owner",
        questions = listOf(
            Question(
                id = 1,
                content = "Question 1",
                image = "Image URL",
                answers = Answer.TrueFalse(true),
                user = "User"
            ),
        )
    ),
    Quiz(
        id = 2,
        title = "Title 2",
        image = "Image URL",
        owner = "Owner",
        questions = listOf(
            Question(
                id = 1,
                content = "Question 2",
                image = "Image URL",
                answers = Answer.TrueFalse(true),
                user = "User"
            ),
            Question(
                id = 2,
                content = "Question 3",
                image = "Image URL",
                answers = Answer.SingleChoice(
                    setOf(
                        Pair(true, "Answer 1"),  // Correct answer
                        Pair(false, "Answer 2"), // Incorrect answer
                        Pair(false, "Answer 3")  // Incorrect answer
                    )
                ),
                user = "User"
            ),
        )
    ),
)

@Preview(showBackground = true)
@Composable
fun QuizListScreen(
    quizList: List<Quiz> = quizTeste,
    onSelectQuiz: (Quiz) -> Unit = {},
    onCreateQuiz: () -> Unit = {},
    onEditQuiz: (Quiz) -> Unit = {},
    onDeleteQuiz: (Quiz) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onFilter: (String) -> Unit = {}
) {

    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filterOptions = listOf("True / False", "Single Choice")

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DropdownMenu(
                expanded = false,
                onDismissRequest = {},
                modifier = Modifier.weight(1f)
            ) {
                filterOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedFilter = option
                            onFilter(option)
                        }
                    )
                }
            }

            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    onSearch(it)
                },
                modifier =
                    Modifier
                        .weight(1f),
                placeholder = { Text(stringResource(R.string.search)) },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Search, stringResource(R.string.search))
                }
            )
        }

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
                    onDeleteQuiz = { onDeleteQuiz(quiz) }
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
                contentDescription = stringResource(R.string.create_quiz),
                imageVector = Icons.Filled.Add
            )
            Text(stringResource(R.string.create_quiz_caps))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizCardV2(
    quiz: Quiz,
    onSelectQuiz: (Quiz) -> Unit,
    onEditQuiz: (Quiz) -> Unit,
    onDeleteQuiz: (Quiz) -> Unit
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
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.6f)
                                )
                            )
                        )
                        .fillMaxSize(),
                    //TODO: get image from string and "return" image/file
                    //painter = painterResource(it),
                    painter = painterResource(R.drawable.quizec_1080),
                    contentDescription = "Quiz Image",
                    contentScale = ContentScale.FillWidth
                )
            } ?: Image(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
                    .fillMaxSize(),
                painter = painterResource(R.drawable.quizec_1080),
                contentDescription = "Quiz Image",
                contentScale = ContentScale.FillWidth
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
                    color = Color.Gray
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("View TODO") },
                onClick = {
                    expanded = false
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizCard(
    quiz: Quiz,
    onSelectQuiz: (Quiz) -> Unit,
    onEditQuiz: (Quiz) -> Unit,
    onDeleteQuiz: (Quiz) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                    //TODO: get image from string and "return" image/file
                    //PLACE_HOLDER
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
                text = { Text(stringResource(R.string.view)) },
                onClick = {
                    expanded = false
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