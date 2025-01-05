package pt.isec.amov.quizec.ui.screens.lobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.jan.supabase.createSupabaseClient
import pt.isec.amov.quizec.ui.viewmodels.app.QuizecViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LobbyScreen(
    viewModel: QuizecViewModel,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(viewModel.currentLobbyStarted.value) {
        if (viewModel.currentLobbyStarted.value) {
            viewModel.fetchLobbyQuiz()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(viewModel.currentLobbyPlayers.toList()) {
                Text(it.username)
            }
        }

        if (!viewModel.currentLobbyStarted.value) {
            ContainedLoadingIndicator()
        } else {
            Text("Quiz: ${viewModel.currentQuiz.value.toString()}")
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