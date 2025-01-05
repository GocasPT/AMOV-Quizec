package pt.isec.amov.quizec.ui.screens.question.manage

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Answer.Drag
import pt.isec.amov.quizec.model.question.Answer.FillBlank
import pt.isec.amov.quizec.model.question.Answer.Matching
import pt.isec.amov.quizec.model.question.Answer.MultipleChoice
import pt.isec.amov.quizec.model.question.Answer.Ordering
import pt.isec.amov.quizec.model.question.Answer.SingleChoice
import pt.isec.amov.quizec.model.question.Answer.TrueFalse
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionType
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.utils.FileUtils
import java.io.File

val quizView2 = listOf(
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
            Question(
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
            ),
            Question(
                id = 3,
                content = "Quantos anodsad asd sa dsads tem o Buno?",
                image = "Image URL",
                answers = Answer.MultipleChoice(
                    setOf(
                        Pair(true, "20"),
                        Pair(false, "21"),
                        Pair(true, "22"),
                    )
                ),
                user = "User"
            ),
            Question(
                id = 4,
                content = "Quantos anos tem o Buno?",
                image = "Image URL",
                answers = Answer.Matching(
                    setOf(
                        Pair("1", "20"),
                        Pair("2", "21"),
                        Pair("3", "22"),
                    )
                ),
                user = "User"
            ),
            Question(
                id = 5,
                content = "Quantos anos tem o Buno?",
                image = "Image URL",
                answers = Answer.Ordering(
                    listOf("23", "21", "22")
                ),
                user = "User"
            ),
            Question(
                id = 6,
                content = "Quantos anos tem o Buno?",
                image = "Image URL",
                answers = Answer.Drag(
                    setOf(
                        Pair(1, "20"),
                        Pair(2, "21"),
                        Pair(3, "22"),
                    )
                ),
                user = "User"
            ),
            Question(
                id = 7,
                content = "Quantos anos tem o Buno?",
                image = "Image URL",
                answers = Answer.FillBlank(
                    setOf(
                        Pair(1, "20"),
                        Pair(2, "21"),
                        Pair(3, "22"),
                    )
                ),
                user = "User"
            ),
        )
    )
)


@Preview(showBackground = true)
@Composable
fun ManageQuestionScreen(
    question: Question? = quizView2[0].questions?.get(2),
    userId : String = "",
    saveQuestion: (Question) -> Unit = {},
    onBack : () -> Unit = {}
) {
    var questionContent by remember { mutableStateOf(question?.content ?: "") }
    var questionAnswers by remember { mutableStateOf(question?.answers ?: TrueFalse(false)) }

    var isExpanded by remember { mutableStateOf(false) }
    var saveEnabled by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    var picture by remember { mutableStateOf(question?.image) }
    val context = LocalContext.current
    val imagePath : String by lazy { FileUtils.getTempFilename(context)}

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
               picture = FileUtils.createFileFromUri(
                    uri = uri,
                    context = context
                )
            }
        }
    )

    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                picture = FileUtils.copyFile(context, imagePath)
            }
        }
    )

    Column(
        modifier = Modifier
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
        }

        Text(
            text = if (question == null) "New Question" else "Edit Question",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = questionContent,
            onValueChange = { questionContent = it },
            label = { Text("Question") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        QuestionTypeDropdown(
            currentAnswer = questionAnswers,
            isExpanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            onAnswerSelected = {
                questionAnswers = when (it) {
                    QuestionType.TRUE_FALSE -> TrueFalse(false)
                    QuestionType.SINGLE_CHOICE -> SingleChoice(emptySet())
                    QuestionType.MULTIPLE_CHOICE -> MultipleChoice(emptySet())
                    QuestionType.MATCHING -> Matching(emptySet())
                    QuestionType.ORDERING -> Ordering(emptyList())
                    QuestionType.DRAG -> Drag(emptySet())
                    //P07 - Not implemented
                    QuestionType.FILL_BLANK -> FillBlank(emptySet())
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {

            Button(
                onClick = {
                    pickImage.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Select Image"
                )
            }

            Button(
                onClick = {
                    takePicture.launch(
                        FileProvider.getUriForFile(
                            context,
                            "pt.isec.amov.quizec.android.fileprovider",
                            File(imagePath)
                        )
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Take Picture"
                )
            }
        }

        Column (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (picture != null) {
                Log.d("PictureDebug", "Picture URI: $picture")
                AsyncImage(
                    model = picture,
                    contentDescription = "Question's image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                )
            }
        }
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
        ) {
            when (questionAnswers) {
                is TrueFalse -> YesNoQuestion(
                    initialAnswer = questionAnswers as TrueFalse,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it }
                )

                is SingleChoice -> SingleChoiceQuestion(
                    initialAnswer = questionAnswers as SingleChoice,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState
                )

                is MultipleChoice -> MultipleChoiceQuestion(
                    initialAnswer = questionAnswers as MultipleChoice,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState
                )

                is Matching -> MatchingQuestion(
                    initialAnswer = questionAnswers as Matching,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState
                )

                is Ordering -> OrderingQuestion(
                    initialAnswer = questionAnswers as Ordering,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState
                )

                is Drag -> DragQuestion(
                    initialAnswer = questionAnswers as Drag,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    modifier = Modifier.weight(1f),
                    questionTitle = questionContent
                )

                is FillBlank -> FillBlankQuestion(
                    initialAnswer = questionAnswers as FillBlank,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    modifier = Modifier.weight(1f),
                    questionTitle = questionContent
                )
            }
        }

        Button(
            onClick = {
                saveQuestion(
                    question?.copy(
                        content = questionContent,
                        image = picture,
                        answers = questionAnswers
                    ) ?: Question(
                        id = null,
                        image = picture,
                        content = questionContent,
                        answers = questionAnswers,
                        user = userId
                    )
                )
            },
            modifier = Modifier.padding(top = 16.dp),
            enabled = saveEnabled && questionContent.isNotBlank()
        ) {
            Text(if (question == null) "Create Question" else "Save Changes")
        }
    }
}