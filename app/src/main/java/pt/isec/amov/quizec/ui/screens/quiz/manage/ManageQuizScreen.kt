package pt.isec.amov.quizec.ui.screens.quiz.manage

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.utils.FileUtils
import java.io.File

val questions = listOf(
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
@Preview(showBackground = true)
@Composable
fun ManageQuizScreen(
    quiz: Quiz? = null,
    userId : String = "1",
    questionList: List<Question> = questions,
    saveQuiz: (Quiz) -> Unit = {}
) {
    var quizTitle by remember { mutableStateOf(quiz?.title ?: "") }
    //var maxTimeMinutes by remember { mutableStateOf(quiz?.maxTime?.toString() ?: "") }
    //var isActive by remember { mutableStateOf(quiz?.isActive ?: true) }
    //var locationRestricted by remember { mutableStateOf(quiz?.locationRestricted ?: false) }
    //var immediateResults by remember { mutableStateOf(quiz?.immediateResults ?: true) }
    val selectedQuestions = remember {
        mutableStateListOf<Question>().apply {
            quiz?.questions?.let { addAll(it) }
        }
    }
    val scrollState = rememberScrollState()

    var picture by remember { mutableStateOf(quiz?.image) }
    val context = LocalContext.current
    val imagePath : String by lazy { FileUtils.getTempFilename(context)}

    var showDialog by remember { mutableStateOf(false) }

    val pickImage = rememberLauncherForActivityResult(
        //contrato especifico para escolher uma imagem/video
        //podemos depois usar outros para escolher ficheiros de acordo com o formato e etc
        // *.doc; *image/*; *video/*; etc
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                //uri é o caminho para a imagem
                //uri.toString() é o caminho para a imagem em string
                //para irmos ao internal storage, temos que indicar o contexto da aplicação
                //o contexto é a nossa atividade, que é o nosso MainActivity
                //no entanto, para isso precisavamos de mudar todos os parametros para trás disso de forma a incluir a atividade por parametro neles todos ate aqui
                //por isso, o jetpack compose oferece o LocalContext.current (assim é generico, dá para as atividade todas) - variavel criada anteriormente
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

    fun isFormValid(): Boolean {
        return quizTitle.isNotEmpty() &&
                //maxTimeMinutes.isNotEmpty() &&
                //maxTimeMinutes.all { it.isDigit() } &&
                selectedQuestions.isNotEmpty()
    }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (quiz == null) "New Quiz" else "Edit Quiz",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp),
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = quizTitle,
                onValueChange = { quizTitle = it },
                label = { Text("Quiz Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Column (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (picture != null) {
                    Log.d("PictureDebug", "Picture URI: $picture")
                    AsyncImage(
                        model = picture,
                        contentDescription = "Quiz's image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                }

                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(Icons.Default.Image, contentDescription = "Add Image")
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Select Image") },
                    text = { Text("Would you like to select an image or take a new one?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }) {
                            Text("Select Image")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDialog = false
                            takePicture.launch(
                                FileProvider.getUriForFile(
                                    context,
                                    "pt.isec.amov.quizec.android.fileprovider",
                                    File(imagePath)
                                )
                            )
                        }) {
                            Text("Take Picture")
                        }
                    }
                )
            }

            //FullImagePicker()

//
//        picture?.let { picture ->
//            Text(
//                text = "Image selected",
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//            )
//            AsyncImage(
//                model = picture,
//                contentDescription = "Quiz's image",
//                modifier = Modifier
//                    .aspectRatio(1f)
//                    .clip(RoundedCornerShape(18.dp))
//                    .align(Alignment.CenterHorizontally)
//            )
//        }
//
//        Button(
//            onClick = {
//                takePicture.launch(
//                    FileProvider.getUriForFile(
//                        context,
//                        "pt.isec.amov.quizec.android.fileprovider",
//                        File(imagePath)
//                    )
//                )
//            },
//            modifier = Modifier.weight(1f)
//        ) {
//            Icon(
//                imageVector = Icons.Filled.Add,
//                contentDescription = "Take picture"
//            )
//        }

            /*
            OutlinedTextField(
                value = maxTimeMinutes,
                onValueChange = {
                    if (it.isEmpty() || it.all { char -> char.isDigit() }) {
                        maxTimeMinutes = it
                    }
                },
                label = { Text("Max Time (minutes)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Start Immediately")
                Switch(
                    checked = isActive,
                    onCheckedChange = { isActive = it }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Restrict Location")
                Switch(
                    checked = locationRestricted,
                    onCheckedChange = { locationRestricted = it }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Show Results Immediately")
                Switch(
                    checked = immediateResults,
                    onCheckedChange = { immediateResults = it }
                )
            }
            */
//
//        Text(
//            text = "Select Questions",
//            style = MaterialTheme.typography.bodyLarge,
//            modifier = Modifier
//                .padding(top = 16.dp),
//        )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = questionList,
                    key = { it.hashCode() }
                ) { question ->
                    QuestionCard(
                        question = question,
                        isSelected = selectedQuestions.contains(question),
                        onToggle = {
                            if (selectedQuestions.contains(question)) {
                                selectedQuestions.remove(question)
                            } else {
                                selectedQuestions.add(question)
                            }
                        }
                    )
                }
            }

            Button(
                onClick = {
                    val updatedQuiz = quiz?.copy(
                        title = quizTitle,
                        questions = selectedQuestions,
                        //isActive = isActive,
                        //maxTime = maxTimeMinutes.toLongOrNull(),
                        //locationRestricted = locationRestricted,
                        //immediateResults = immediateResults
                    ) ?: Quiz(
                        id = null,
                        title = quizTitle,
                        image = null,
                        questions = selectedQuestions,
                        owner = userId
                        //isActive = isActive,
                        //maxTime = maxTimeMinutes.toLongOrNull(),
                        //locationRestricted = locationRestricted,
                        //immediateResults = immediateResults
                    )
                    saveQuiz(updatedQuiz)
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                enabled = isFormValid()
            ) {
                Text(if (quiz == null) "Create Quiz" else "Save Changes")
            }
        }
}

@Composable
fun QuestionCard(
    question: Question,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onToggle),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(254, 95, 85) else Color(153, 247, 171)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
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
            Text(
                text = question.answers.answerType.displayName,
                modifier = Modifier.padding(end = 16.dp)
            )
            Icon(
                imageVector = if (isSelected) Icons.Default.Remove else Icons.Default.Add,
                contentDescription = if (isSelected) "Remove" else "Add"
            )
        }
    }
}