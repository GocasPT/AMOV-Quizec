package pt.isec.amov.quizec.ui.screens.auth

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.ui.viewmodels.QuizecViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import pt.isec.amov.quizec.ui.viewmodels.QuizecAuthViewModel

@Composable
fun LoginScreen(
    viewModel: QuizecAuthViewModel,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {

    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)
        LoginScreenLandscape(modifier = modifier, onSuccess = onSuccess, viewModel = viewModel)
    else
        LoginScreenPortrait(modifier = modifier, onSuccess = onSuccess, viewModel = viewModel)

}

@Composable
fun LoginScreenLandscape(
    viewModel: QuizecAuthViewModel,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {}

@Composable
fun LoginScreenPortrait(
    viewModel: QuizecAuthViewModel,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.user.value) {
        if (viewModel.user.value != null && viewModel.error.value == null) {
            Log.d("LoginScreen", "going to enter in with ${viewModel.user.value} and ${password.value}")
            onSuccess()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "logo",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = email.value,
                onValueChange = { newText -> email.value = newText },
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(percent = 20),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                ),
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { newText -> password.value = newText },
                label = { Text(stringResource(R.string.password)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(percent = 20),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password,
                ),
                trailingIcon = {
                    if (showPassword) {
                        IconButton(
                            onClick = { showPassword = false }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "Hide Password"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { showPassword = true }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "Hide Password"
                            )
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (viewModel.error.value != null) {
                Text(
                    text = "Error: ${viewModel.error.value}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .background(Color(255, 0, 0))
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Button(
                onClick = {
                    viewModel.signInWithEmail(email.value, password.value)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("LOGIN")
            }
        }

        Text(
            text = "Don't have an account? Click here to register.",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}