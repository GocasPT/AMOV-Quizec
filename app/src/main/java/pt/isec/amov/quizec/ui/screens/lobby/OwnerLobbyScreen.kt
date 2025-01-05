package pt.isec.amov.quizec.ui.screens.lobby

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.jan.supabase.createSupabaseClient
import pt.isec.amov.quizec.ui.viewmodels.app.QuizecViewModel

@Composable
fun OwnerLobbyScreen(
    viewModel: QuizecViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Text("Lobby: ${viewModel.currentLobby.value?.code}")
        Text("Players: ${viewModel.currentLobbyPlayerCount.value}")
    }
}

@Preview(showBackground = true)
@Composable
fun OwnerLobbyScreenPreview() {
    OwnerLobbyScreen(
        viewModel = QuizecViewModel(
            createSupabaseClient("", "") {}
        ),
    )
}