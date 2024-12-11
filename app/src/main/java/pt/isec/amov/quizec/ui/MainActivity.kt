package pt.isec.amov.quizec.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import pt.isec.amov.quizec.QuizecApp
import pt.isec.amov.quizec.ui.theme.QuizecTheme
import pt.isec.amov.quizec.ui.viewmodels.MainScreen
import pt.isec.amov.quizec.ui.viewmodels.QuizecViewModel
import pt.isec.amov.quizec.ui.viewmodels.QuizecViewModelFactory

//******
/*@Serializable
data class Country(
    val id: Int,
    val name: String,
)*/
//**********

class MainActivity : ComponentActivity() {
    private val app: QuizecApp by lazy { application as QuizecApp }
    private val viewModel: QuizecViewModel by viewModels { QuizecViewModelFactory(app.questionList, app.quizList) }
    private val dbClient: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "https://vivtabksraqgsyzulwgz.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZpdnRhYmtzcmFxZ3N5enVsd2d6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzM4NzIyMTEsImV4cCI6MjA0OTQ0ODIxMX0.ZH3W7Ar1L7HGAmifvFdkJ7UuQiVRrNk8ndS5viBfKrc"
        ) {
            install(Postgrest)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizecTheme {
                MainScreen(dbClient, viewModel)
            }
        }

        //TODO: add permissions requests checkers
    }
}