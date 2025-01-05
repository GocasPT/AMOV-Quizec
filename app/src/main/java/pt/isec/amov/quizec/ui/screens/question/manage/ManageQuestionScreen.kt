package pt.isec.amov.quizec.ui.screens.question.manage

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage

import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.question.Answer.Drag
import pt.isec.amov.quizec.model.question.Answer.FillBlank
import pt.isec.amov.quizec.model.question.Answer.Matching
import pt.isec.amov.quizec.model.question.Answer.MultipleChoice
import pt.isec.amov.quizec.model.question.Answer.Ordering
import pt.isec.amov.quizec.model.question.Answer.SingleChoice
import pt.isec.amov.quizec.model.question.Answer.TrueFalse
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionType
import pt.isec.amov.quizec.utils.FileUtils
import java.io.File

@Composable
fun ManageQuestionScreen(
    question: Question?,
    userId: String,
    saveQuestion: (Question) -> Unit,
    onBack: () -> Unit
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
            text = if (question == null) stringResource(R.string.new_question) else stringResource(R.string.edit_question),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = questionContent,
            onValueChange = { questionContent = it },
            label = { Text(stringResource(R.string.question)) },
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

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
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
                )

                is MultipleChoice -> MultipleChoiceQuestion(
                    initialAnswer = questionAnswers as MultipleChoice,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                )

                is Matching -> MatchingQuestion(
                    initialAnswer = questionAnswers as Matching,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                )

                is Ordering -> OrderingQuestion(
                    initialAnswer = questionAnswers as Ordering,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                )

                is Drag -> DragQuestion(
                    initialAnswer = questionAnswers as Drag,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
                    questionTitle = questionContent
                )

                is FillBlank -> FillBlankQuestion(
                    initialAnswer = questionAnswers as FillBlank,
                    onAnswerChanged = { questionAnswers = it },
                    saveEnabled = { saveEnabled = it },
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
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally),
            enabled = saveEnabled && questionContent.isNotBlank()
        ) {
            Text(if (question == null) stringResource(R.string.create_question) else stringResource(R.string.save_changes))
        }
    }
}}