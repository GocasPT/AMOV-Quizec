package pt.isec.amov.quizec.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pt.isec.amov.quizec.QuizecApp
import pt.isec.amov.quizec.ui.theme.QuizecTheme
import pt.isec.amov.quizec.ui.viewmodels.MainScree

class MainActivity : ComponentActivity() {
    val app: QuizecApp by lazy { application as QuizecApp }
    //TODO: add viewModel variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizecTheme {
                MainScree()
            }
        }

        //TODO: add permissions requests checkers
    }
}