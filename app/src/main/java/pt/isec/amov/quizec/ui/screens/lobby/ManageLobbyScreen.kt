package pt.isec.amov.quizec.ui.screens.lobby

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.createSupabaseClient
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.ui.viewmodels.app.QuizecViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageLobbyScreen(
    viewModel: QuizecViewModel,
    isNewLobby: Boolean = true,
    onCreateLobby: (Long, Boolean, Boolean, Long) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val availableQuizes = viewModel.quizList
    var expanded by remember { mutableStateOf(false) }
    var selectedQuiz by remember { mutableStateOf(viewModel.currentQuiz.value) }

    var sliderTime by remember { mutableStateOf(30f) }
    var instantStart by remember { mutableStateOf(false) }
    var isLocationRestricted by remember { mutableStateOf(false) }
    var sliderLocation by remember { mutableStateOf(60f) }
    var isInstantScore by remember { mutableStateOf(true) }

    //verificar isto
//    val sendIntent = Intent(Intent.ACTION_SEND).apply {
//        putExtra(Intent.EXTRA_TEXT, text)
//        type = "text/plain"
//    }
//    val shareIntent = Intent.createChooser(sendIntent, null)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        if (isNewLobby)
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                    label = { Text("Selecione um questionário") }, //TODO: resource lang
                    value = selectedQuiz?.title ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableQuizes.forEach { quiz ->
                        DropdownMenuItem(
                            text = { Text(text = quiz.title) },
                            onClick = {
                                selectedQuiz = quiz
                                expanded = false
                                Toast.makeText(context, quiz.title, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        //}
        else
            Text(
                text = "${selectedQuiz?.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
            )

        selectedQuiz?.image?.let {
            Image(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                painter = painterResource(R.drawable.quizec_1080),
                //painter = rememberImagePainter(quiz.image),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "QUIZ ID", //!TODO: change this
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                )
                Button(
                    onClick = { /*TODO*/ },
                    //TODO: when share, disable settings inputs
                    //startActivity(context, shareIntent, null)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = null,
                    )
                }
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 12.dp),
                text = stringResource(R.string.quiz_time),
            )
            Slider(
                value = sliderTime,
                onValueChange = { sliderTime = it },
                valueRange = 1f..240f,
            )
            Text(
                text = "${sliderTime.toInt()} min",
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.instant_start),
            )
            Switch(
                checked = instantStart,
                onCheckedChange = { instantStart = !instantStart }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.location_restricted),
            )
            Switch(
                checked = isLocationRestricted,
                onCheckedChange = { isLocationRestricted = !isLocationRestricted }
            )
        }

        isLocationRestricted.let {
            if (it) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Slider(
                        value = sliderLocation,
                        onValueChange = { sliderLocation = it },
                        valueRange = 0f..100f,
                        steps = 20,
                    )
                    Text(
                        text = "$sliderLocation km",
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.instant_score),
            )
            Switch(
                checked = isInstantScore,
                onCheckedChange = { isInstantScore = !isInstantScore }
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //!TODO: disable button when selectedQuiz is null
            Button(
                onClick = {
                    onCreateLobby(
                        selectedQuiz!!.id!!.toLong(),
                        instantStart,
                        isLocationRestricted,
                        sliderTime.toLong()
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = null,
                )
            }
        }

        Text("SHARE")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Demo_ExposedDropdownMenuBox() {
    val context = LocalContext.current

    val availableQuizes = arrayOf("Quiz 1", "Quiz 2", "Quiz 3", "Quiz 4", "Quiz 5")
    var expanded by remember { mutableStateOf(false) }
    val selectedQuiz by remember { mutableStateOf(availableQuizes[0]) }

    val coffeeDrinks = arrayOf("Americano", "Cappuccino", "Espresso", "Latte", "Mocha")
    var selectedText by remember { mutableStateOf(coffeeDrinks[0]) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                coffeeDrinks.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManageLobbyScreenPreview_SelectQuiz() {
    ManageLobbyScreen(
        viewModel = QuizecViewModel(
            createSupabaseClient("", "") {}
        ),
        isNewLobby = false,
        onCreateLobby = { _, _, _, _ -> },
    )
}

@Preview(showBackground = true)
@Composable
fun ManageLobbyScreenPreview_PreSelectedQuiz() {
    ManageLobbyScreen(
        viewModel = QuizecViewModel(
            createSupabaseClient("", "") {}
        ),
        isNewLobby = true,
        onCreateLobby = { _, _, _, _ -> },
    )
}