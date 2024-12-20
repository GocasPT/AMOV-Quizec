package pt.isec.amov.quizec.ui.screens.question.manage

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import pt.isec.a2021138502.contacts_storage.utils.FileUtils
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
import java.io.File

@Composable
fun ManageQuestionScreen(
    //TODO: receive question OR received each field of question? (MutableState<?>)
    question: Question?,
    saveQuestion: (Question) -> Unit
) {
    val context = LocalContext.current
    var questionContent: String by remember { mutableStateOf(question?.content ?: "") }
    val picture: MutableState<String?> =
        remember { mutableStateOf(null) } //TODO: maybe change this...
    val imagePath: String by lazy { FileUtils.getTempFilename(context) }
    var questionAnswers: Answer by remember {
        mutableStateOf(
            question?.answers ?: TrueFalse(false)
        )
    }

    val pickImage =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                uri?.let {
                    //question?.image = FileUtils.createFileFromUri(context, it)
                    picture.value = FileUtils.createFileFromUri(context, it)
                    Log.d(
                        "ManageQuestionScreen",
                        "File: ${FileUtils.createFileFromUri(context, it)}"
                    )
                }
            })
    val takePicture =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                if (success)
                //question?.image.value = FileUtils.copyFile(context, imagePath)
                    picture.value = FileUtils.copyFile(context, imagePath)
                Log.d("ManageQuestionScreen", "File: ${FileUtils.copyFile(context, imagePath)}")

            })

    var isExpanded by remember { mutableStateOf(false) }
    var saveEnabled by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (question == null) "New Question" else "Edit Question",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            OutlinedTextField(
                value = questionContent,
                onValueChange = { questionContent = it },
                label = { Text("Question Text") },
                /*modifier = Modifier
                    .padding(16.dp)*/
            )
            IconButton(
                onClick = {
                    pickImage.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(Icons.Filled.Image, contentDescription = "Pick Image")
            }
            IconButton(
                onClick = {
                    takePicture.launch(
                        FileProvider.getUriForFile(
                            context,
                            "pt.isec.amov.quizec.android.fileprovider",
                            File(imagePath)
                        )
                    )
                },
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(Icons.Filled.AddAPhoto, contentDescription = "Take Picture")
            }
        }

        //TODO: image view (small) with delete button
        //question?.image?.let { picture ->
        picture.value?.let { picture ->
            AsyncImage(
                model = picture,
                contentDescription = "Contact's picture",
                modifier = Modifier
                    .fillMaxSize(0.5f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally),
            )
        }

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

        Button(
            onClick = {
                saveQuestion(
                    question?.copy(
                        content = questionContent,
                        answers = questionAnswers
                    ) ?: Question(
                        //id = QuestionIDGenerator.getNextId(),
                        image = picture.value,
                        content = questionContent,
                        answers = questionAnswers
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