package pt.isec.amov.quizec.ui.screens.auth

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.model.User
import pt.isec.amov.quizec.ui.viewmodels.QuizecAuthViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun RegisterScreen(
    viewModel: QuizecAuthViewModel,
    playerInfo: User,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {

    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)
        RegisterScreenLandscape(modifier = modifier, onSuccess = onSuccess, playerInfo = playerInfo, viewModel = viewModel)
    else
        RegisterScreenPortrait(modifier = modifier, onSuccess = onSuccess, playerInfo = playerInfo, viewModel = viewModel)

}

@Composable
fun RegisterScreenLandscape(
    viewModel: QuizecAuthViewModel,
    playerInfo: User,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {

}

@Composable
fun RegisterScreenPortrait(
    viewModel: QuizecAuthViewModel,
    playerInfo: User,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    var showRepeatPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(viewModel.user.value) {
        if (viewModel.user.value != null && viewModel.error.value == null) {
            onSuccess()
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = name.value,
                onValueChange = { newText -> name.value = newText },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(percent = 20),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = email.value,
                onValueChange = { newText -> email.value = newText },
                label = { Text("Name") },
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
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(percent = 20),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password
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
            OutlinedTextField(
                value = repeatPassword,
                onValueChange = { newText -> repeatPassword = newText },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(percent = 20),
                visualTransformation = if (showRepeatPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password
                ),
                trailingIcon = {
                    if (showRepeatPassword) {
                        IconButton(
                            onClick = { showRepeatPassword = false }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = "Hide Password"
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { showRepeatPassword = true }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = "Hide Password"
                            )
                        }
                    }
                }
            )

        }

        if (viewModel.error.value != null) {
            Text(
                text = "Error: ${viewModel.error.value}",
                modifier = Modifier
                    .background(Color(255, 0, 0))
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    viewModel.createUserWithEmail(name.value, email.value, password.value)

//
//                    if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank() && repeatPassword.isNotBlank() && (password.equals(repeatPassword))) {
//                        User(name, email, password, repeatPassword)
//                    } else {
//                        errorMessage = "Please fill in the blanks."
//                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("REGISTER")
            }
        }
    }
}