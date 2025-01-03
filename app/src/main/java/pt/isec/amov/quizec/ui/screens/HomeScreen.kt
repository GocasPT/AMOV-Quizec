package pt.isec.amov.quizec.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun HomeScreen(
    onJoinLobby: (String) -> Unit,
    onCreateLobby: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)
        HomeScreenLandscape(
            onJoinLobby = onJoinLobby,
            onCreateLobby = onCreateLobby,
            modifier = modifier
        )
    else
        HomeScreenPortrait(
            onJoinLobby = onJoinLobby,
            onCreateLobby = onCreateLobby,
            modifier = modifier
        )
}

@Composable
fun HomeScreenLandscape(
    onJoinLobby: (String) -> Unit,
    onCreateLobby: () -> Unit,
    modifier: Modifier = Modifier,
) {

}

@Composable
fun HomeScreenPortrait(
    onJoinLobby: (String) -> Unit,
    onCreateLobby: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val code = remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Welcome, TIAGO",
            style = MaterialTheme.typography.titleLarge
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = code.value,
                onValueChange = { if (it.length <= 6) code.value = it },
                label = { Text("JOIN QUIZ:") },
                textStyle = TextStyle(fontSize = 48.sp),
                singleLine = true,
                shape = RoundedCornerShape(percent = 20),
                //TF TODO: se usarmos o teclado do pc da para meter letras
                //tF TODO: experimentar sem o Default.Copy para ver o q faz
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { onJoinLobby(code.value) },
                        enabled = code.value.length == 6,
                        modifier = Modifier
                            .padding(16.dp)
                            .background(
                                color =
                                if (code.value.length == 6) Color(171, 7, 7, 255)
                                else Color.Gray,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Check",
                            tint = Color(255, 255, 255, 255)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            ElevatedButton(
                onClick = onCreateLobby, //TODO: popup/modal OR QuizListScreen?
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("CREATE ROOM ")
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onJoinLobby = {},
        onCreateLobby = {},
        modifier = Modifier.fillMaxSize()
    )
}