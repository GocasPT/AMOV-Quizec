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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.amov.quizec.R


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    ) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)
        HomeScreenLandscape(modifier = modifier)
    else
        HomeScreenPortrait(modifier = modifier)
}

@Composable
fun HomeScreenLandscape(
    modifier: Modifier = Modifier,
    ) {

}

@Composable
fun HomeScreenPortrait(
    modifier: Modifier = Modifier,
    ) {

    val code = remember { mutableStateOf("") }
    val welcomeText = stringResource(id = R.string.welcome)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "$welcomeText Tiago",
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
                label = { Text(stringResource(R.string.join_quiz)) },
                textStyle = TextStyle(fontSize = 48.sp),
                singleLine = true,
                shape = RoundedCornerShape(percent = 20),
                trailingIcon = {
                    IconButton(
                        onClick = { /*TODO*/ },
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
                            contentDescription = stringResource(R.string.check),
                            tint = Color(255, 255, 255, 255)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            ElevatedButton(
                onClick = {/*TODO*/},
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.create_room))
            }

        }
    }
}