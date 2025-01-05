package pt.isec.amov.quizec.ui.screens.lobby

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.jan.supabase.createSupabaseClient
import pt.isec.amov.quizec.ui.screens.quiz.live.QuizLiveScreen
import pt.isec.amov.quizec.ui.viewmodels.app.QuizecViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LobbyScreen(
    viewModel: QuizecViewModel,
    modifier: Modifier = Modifier,
) {
    var questionIndex by remember { mutableIntStateOf(0) }
    val quiz = viewModel.currentQuiz.value
    val hasStarted = viewModel.currentLobbyStarted.value

    LaunchedEffect(hasStarted) {
        if (hasStarted) {
            viewModel.fetchLobbyQuiz()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            !hasStarted -> {
                Text("Waiting for quiz to start")
                ContainedLoadingIndicator()
            }

            quiz == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                quiz.questions?.getOrNull(questionIndex)?.let { currentQuestion ->
                    LinearProgressIndicator(
                        progress = { (questionIndex + 1).toFloat() / (quiz.questions?.size ?: 1) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    )

                    QuizLiveScreen(
                        question = currentQuestion,
                        onAnswerSelected = { answer ->
                            Log.d("LobbyScreen", "Answer selected: $answer")
                        },
                        onNextQuestion = {
                            questionIndex += 1
                        }
                    )
                } ?: run {
                    Text("Quiz Ended")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LobbyScreenPreview() {
    LobbyScreen(
        viewModel = QuizecViewModel(
            createSupabaseClient("", "") {}
        ),
    )
}