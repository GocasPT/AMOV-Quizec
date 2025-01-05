package pt.isec.amov.quizec.ui.screens.auth

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pt.isec.amov.quizec.R

@Composable
fun RegisterScreen(
    onRegister: (String, String, String, String) -> Unit,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessageText : String?,
    clearError : () -> Unit
) {

    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)
        RegisterScreenLandscape(
            modifier = modifier,
            onRegister = onRegister,
            onSuccess = onSuccess,
            onBack = onBack,
            errorMessageText = errorMessageText,
            clearError = clearError
        )
    else
        RegisterScreenPortrait(
            modifier = modifier,
            onRegister = onRegister,
            onSuccess = onSuccess,
            onBack = onBack,
            errorMessageText = errorMessageText,
            clearError = clearError
        )
}

@Composable
fun RegisterScreenLandscape(
    onRegister: (String, String, String, String) -> Unit,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessageText : String?,
    clearError: () -> Unit
) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val repeatPassword = remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showRepeatPassword by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(errorMessageText) {
        if(errorMessageText.equals("Success")) {
            showDialog = true
        }
    }

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { newText -> name.value = newText },
                        label = { Text(stringResource(R.string.name)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(percent = 20),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { newText -> email.value = newText },
                        label = { Text(stringResource(R.string.email)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(percent = 20),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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
                        keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = repeatPassword.value,
                        onValueChange = { newText -> repeatPassword.value = newText },
                        label = { Text(stringResource(R.string.repeat_password)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(percent = 20),
                        visualTransformation = if (showRepeatPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { showRepeatPassword = !showRepeatPassword }) {
                                Icon(
                                    imageVector = if (showRepeatPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        shape = RoundedCornerShape(percent = 20),
                        onClick = {
                            onRegister(name.value, email.value, password.value, repeatPassword.value)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D1C1F)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.register))
                    }

                    if (showDialog) {
                        Dialog(onDismissRequest = { }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(R.string.registration_successful),
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            showDialog = false
                                            onSuccess()
                                        },
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    ) {
                                        Text(stringResource(R.string.ok))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    SnackBar(
                        error = errorMessageText,
                        clearError = clearError,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .clickable { onBack() },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back to Login",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.back_to_login),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

}

@Composable
fun RegisterScreenPortrait(
    onRegister: (String, String, String, String) -> Unit,
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessageText : String?,
    clearError: () -> Unit
) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val repeatPassword = remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showRepeatPassword by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(errorMessageText) {
        if(errorMessageText.equals("Success")) {
            showDialog = true
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 32.dp)
                .clickable { onBack() },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back to Login",
                modifier = Modifier
                    .size(24.dp)
            )
            Spacer(
                modifier = Modifier
                    .width(8.dp))
            Text(
                text = stringResource(R.string.back_to_login),
                style = MaterialTheme.typography.bodyMedium,
            )
        }

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
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(percent = 20),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
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
                value = repeatPassword.value,
                onValueChange = { newText -> repeatPassword.value = newText },
                label = { Text(stringResource(R.string.repeat_password)) },
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

            if (showDialog) {
                Dialog(onDismissRequest = { }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.registration_successful),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    showDialog = false
                                    onSuccess()
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(stringResource(R.string.ok))
                            }
                        }
                    }
                }
            }
        }

        Button(
            shape = RoundedCornerShape(percent = 20),
            onClick = {
                onRegister(name.value, email.value, password.value, repeatPassword.value)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D1C1F)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
        ) {
            Text(stringResource(R.string.register))
        }

        SnackBar(
            error = errorMessageText,
            clearError = clearError,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}