package pt.isec.amov.quizec.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import pt.isec.amov.quizec.QuizecApp
import pt.isec.amov.quizec.ui.theme.QuizecTheme
import pt.isec.amov.quizec.ui.viewmodels.MainScreen
import pt.isec.amov.quizec.ui.viewmodels.QuizecViewModel
import pt.isec.amov.quizec.ui.viewmodels.QuizecViewModelFactory

class MainActivity : ComponentActivity() {
    private val app: QuizecApp by lazy { application as QuizecApp }
    private val viewModel: QuizecViewModel by viewModels { QuizecViewModelFactory(app.dbClient) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizecTheme {
                MainScreen(viewModel)
            }
        }

        //TODO: add permissions requests checkers
    }
}