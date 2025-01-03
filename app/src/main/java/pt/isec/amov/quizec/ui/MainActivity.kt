package pt.isec.amov.quizec.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isec.amov.quizec.QuizecApp
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.ui.screens.auth.LoginScreen
import pt.isec.amov.quizec.ui.screens.auth.RegisterScreen
import pt.isec.amov.quizec.ui.theme.QuizecTheme
import pt.isec.amov.quizec.ui.viewmodels.MainScreen
import pt.isec.amov.quizec.ui.viewmodels.QuizecAuthViewModel
import pt.isec.amov.quizec.ui.viewmodels.QuizecViewModel
import pt.isec.amov.quizec.ui.viewmodels.QuizecViewModelAuthFactory
import pt.isec.amov.quizec.ui.viewmodels.QuizecViewModelFactory

class MainActivity : ComponentActivity() {
    companion object {
        const val LOGIN_SCREEN = "Login"
        const val MAIN_SCREEN = "Main"
        const val REGISTER_SCREEN = "Register"
    }

    private val app: QuizecApp by lazy { application as QuizecApp }
    private val viewModel: QuizecViewModel by viewModels { QuizecViewModelFactory(app.dbClient) }
    private val viewModelAuth: QuizecAuthViewModel by viewModels { QuizecViewModelAuthFactory(app.dbClient) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backgroundColorInt = ContextCompat.getColor(this, R.color.defaultBackground)
            val backgroundColor = Color(backgroundColorInt)

            val navController = rememberNavController()
            val user by viewModelAuth.user

            LaunchedEffect(user) {
                if (user != null) {
                    navController.navigate(MAIN_SCREEN) {
                        popUpTo(LOGIN_SCREEN) { inclusive = true }
                    }
                }
            }

            QuizecTheme {
                Surface (
                    color = backgroundColor,
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = LOGIN_SCREEN,
                    ) {
                        composable(LOGIN_SCREEN) {
                            LoginScreen(
                                onLogin = { email, password ->
                                    viewModelAuth.signInWithEmail(email, password)
                                },
                                onRegister = {
                                    navController.navigate(REGISTER_SCREEN)
                                },
                                errorMessageText = viewModelAuth.error.value,
                                clearError = { viewModelAuth.clearError() }
                            )
                        }
                        composable(REGISTER_SCREEN) {
                            RegisterScreen(
                                onRegister = { name, email, password, repeatPassword ->
                                    viewModelAuth.signUpWithEmail(email, password, repeatPassword, name)
                                },
                                onBack = {
                                    navController.navigate(LOGIN_SCREEN) {
                                        popUpTo(REGISTER_SCREEN) { inclusive = true }
                                    }
                                },
                                onSuccess = {
                                    navController.navigate(LOGIN_SCREEN) {
                                        popUpTo(REGISTER_SCREEN) { inclusive = true }
                                    }
                                    viewModelAuth.clearError()
                                },
                                errorMessageText = viewModelAuth.error.value,
                                clearError = { viewModelAuth.clearError() }
                            )
                        }
                        composable(MAIN_SCREEN) {
                            MainScreen(
                                viewModel = viewModel,
                                user = viewModelAuth.user.value,
                                onSignOut = {
                                    viewModelAuth.signOut()
                                    navController.navigate(LOGIN_SCREEN) {
                                        popUpTo(MAIN_SCREEN) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
        //TODO: add permissions requests checkers
    }
}