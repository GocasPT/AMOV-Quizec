package pt.isec.amov.quizec.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import pt.isec.amov.quizec.QuizecApp
import pt.isec.amov.quizec.ui.theme.QuizecTheme
import pt.isec.amov.quizec.ui.viewmodels.MainScree
import pt.isec.amov.quizec.ui.viewmodels.QuizecViewModel
import pt.isec.amov.quizec.ui.viewmodels.QuizecViewModelFactory

class MainActivity : ComponentActivity() {
    val app: QuizecApp by lazy { application as QuizecApp }
    val viewModel: QuizecViewModel by viewModels {
        QuizecViewModelFactory(
            app.questionList,
            app.quizList
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizecTheme {
                MainScree(viewModel)
            }
        }

        //TODO: add permissions requests checkers
    }
}