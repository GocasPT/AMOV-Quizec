package pt.isec.amov.quizec.ui.screens.auth

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.ui.viewmodels.auth.QuizecAuthViewModel

@Composable
fun LoginScreen(
    viewModel: QuizecAuthViewModel,
    onLogin: (String, String) -> Unit,
    onRegister: () -> Unit,
    errorMessageText: String?,
    clearError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)
        LoginScreenLandscape(
            viewModel = viewModel,
            onLogin = onLogin,
            onRegister = onRegister,
            errorMessageText = errorMessageText,
            clearError = clearError,
            modifier = modifier
        )
    else
        LoginScreenPortrait(
            viewModel = viewModel,
            onLogin = onLogin,
            onRegister = onRegister,
            errorMessageText = errorMessageText,
            clearError = clearError,
            modifier = modifier
        )
}

@Composable
fun LoginScreenLandscape(
    viewModel: QuizecAuthViewModel,
    onLogin: (String, String) -> Unit,
    onRegister: () -> Unit,
    errorMessageText: String?,
    clearError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val gradientColors = listOf(Cyan, Blue)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .align(Alignment.CenterVertically),
                painter = painterResource(R.drawable.quizec_1080),
                contentDescription = "logo_image"
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
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
                    )
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
                        IconButton(
                            onClick = { showPassword = !showPassword }
                        ) {
                            Icon(
                                imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (showPassword) "Hide Password" else "Show Password"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    shape = RoundedCornerShape(percent = 20),
                    onClick = {
                        onLogin(email.value, password.value)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D1C1F))
                ) {
                    Text(stringResource(R.string.login))
                }
            }
        }

        BasicText(
            text = buildAnnotatedString {
                append(stringResource(R.string.don_t_have_an_account))
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        brush = Brush.linearGradient(
                            colors = gradientColors
                        )
                    )
                ) {
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "register",
                            linkInteractionListener = {
                                onRegister()
                            }
                        )
                    ) {
                        append(stringResource(R.string.click_here_to_register))
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        SnackBar(
            error = errorMessageText,
            clearError = clearError,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}

@SuppressLint("ResourceAsColor")
@Composable
fun LoginScreenPortrait(
    viewModel: QuizecAuthViewModel,
    onLogin: (String, String) -> Unit,
    onRegister: () -> Unit,
    errorMessageText: String?,
    clearError: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val gradientColors = listOf(Cyan, Blue)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            painter = painterResource(R.drawable.quizec_1080),
            contentDescription = "logo_image",
        )

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
            Button(
                shape = RoundedCornerShape(percent = 20),
                onClick = {
                    onLogin(email.value, password.value)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9D1C1F))
            ) {
                Text(stringResource(R.string.login))
            }
        }

        BasicText(
            text = buildAnnotatedString {
                append(stringResource(R.string.don_t_have_an_account))
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        brush = Brush.linearGradient(
                            colors = gradientColors
                        )
                    )
                ) {
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "register",
                            linkInteractionListener = {
                                onRegister()
                            }
                        )
                    ) {
                        append(stringResource(R.string.click_here_to_register))
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            style = MaterialTheme.typography.bodyMedium,
        )

        SnackBar(
            error = errorMessageText,
            clearError = clearError,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}