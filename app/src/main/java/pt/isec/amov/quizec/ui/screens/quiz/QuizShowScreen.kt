package pt.isec.amov.quizec.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.quiz.Quiz

@Composable
fun QuizShowScreen(
    quiz: Quiz,
    questionList: List<Question>,
    onBack: () -> Unit,
    onEdit: (Quiz) -> Unit,
    onCreateLobby: (Long) -> Unit,
    onCreateQuestion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expandAll by remember { mutableStateOf(false) }
    val expandIndividual = remember(questionList) { mutableStateListOf(*Array(questionList.size) {false}) }

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
                    onEdit(quiz)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Quiz",
                    tint = Color.Gray
                )
            }
        }

        Text(
            text = quiz.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            model = quiz.image ?: R.drawable.fundo_exemplo,
            contentDescription = "quizImage",
            contentScale = ContentScale.Crop,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
            )

            IconButton(
                onClick = {
                    expandAll = !expandAll
                    expandIndividual.fill(expandAll)
                }
            ) {
                Icon(
                    modifier = Modifier
                        .wrapContentWidth(),
                    imageVector = if (expandAll) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expandAll) "Collapse All" else "Expand All"
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(),
        ) {
            items (
                items = questionList,
                key = { it.hashCode() }
            ) { question ->
                val index = questionList.indexOf(question)
                QuestionInfoTemp(
                    question = question,
                    showExpanded = expandIndividual[index],
                    onExpandToggle = { expandIndividual[index] = !expandIndividual[index] }
                )
            }
        }
    }
}

@Composable
fun QuestionInfoTemp(
    question: Question,
    showExpanded: Boolean,
    onExpandToggle: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        onClick = onExpandToggle
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question.content,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = question.answers.answerType.icon,
                    contentDescription = question.answers.answerType.toString()
                )
                Icon(
                    imageVector = if (showExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (showExpanded) "Opened" else "Closed"
                )
            }
        }

        if (showExpanded) {
            Column(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //image
                Text(
                    text = question.answers.answerType.displayName
                )
                when (question.answers) {
                    is Answer.TrueFalse -> {
                        Text(
                            text = question.answers.rightAnswer.toString()
                        )
                    }

                    is Answer.Drag -> TODO()

                    is Answer.FillBlank -> {
                        val contentWithBlanks = question.content.split(" ").joinToString(" ") {
                            word ->
                            val correctAnswer = question.answers.answers.find { it.second == word }
                            if (correctAnswer != null) "_".repeat(word.length) else word
                        }

                        Text(
                            text = contentWithBlanks
                        )
                    }

                    is Answer.Matching -> {
                        val correctAnswers = question.answers.pairs
                            .joinToString(", ") { "[${it.first}: ${it.second}]" }
                        Text(
                            text = "Correct Answers: $correctAnswers"
                        )
                    }

                    is Answer.MultipleChoice -> {
                        val correctAnswers = question.answers.answers
                            .filter { it.first }
                            .joinToString { it.second }
                        Text(
                            text = "Correct Answers: $correctAnswers"
                        )
                    }

                    is Answer.Ordering -> {
                        val correctAnswers = question.answers.order
                            .joinToString { it }
                        Text(
                            text = "Correct Order: $correctAnswers"
                        )
                    }

                    is Answer.SingleChoice -> {
                        val correctAnswer = question.answers.answers
                            .filter { it.first }
                            .joinToString { it.second }
                        Text(
                            text = "Correct Answer: $correctAnswer"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizShowScreenPreview() {
    QuizShowScreen(
        quiz = Quiz(
            title = "Quiz Title",
            owner = "Owner",
            id = null,
            image = null,
            questions = listOf(
                Question(null, "Question 1", null, Answer.TrueFalse(true), "Sr. batata"),
                Question(null, "Question 2", null, Answer.SingleChoice(setOf()), "Sr. batata"),
                Question(null, "Question 3", null, Answer.TrueFalse(true), "Sr. batata"),
            )
        ),
        questionList = listOf(
            Question(null, "Question 1", null, Answer.TrueFalse(true), "Sr. batata"),
            Question(null, "Question 2", null, Answer.SingleChoice(setOf()), "Sr. batata"),
            Question(null, "Question 3", null, Answer.TrueFalse(true), "Sr. batata"),
        ),
        onCreateLobby = {},
        onCreateQuestion = {},
        onBack = {},
        onEdit = {}
    )
}