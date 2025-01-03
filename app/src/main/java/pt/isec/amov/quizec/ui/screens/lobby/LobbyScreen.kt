package pt.isec.amov.quizec.ui.screens.lobby

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isec.amov.quizec.model.Lobby
import pt.isec.amov.quizec.ui.viewmodels.QuizecViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LobbyScreen(
    viewModel: QuizecViewModel?,
    lobby: Lobby,
    modifier: Modifier = Modifier,
) {
    var waiting by remember { mutableStateOf(!lobby.started) }
    val playersCount = viewModel?._currentLobbyPlayerCount
    val playersList = viewModel?._currentLobbyPlayers

    LaunchedEffect(Unit) {
        viewModel!!.getPlayerCount()

        Log.d("LobbyScreen", "Waiting for lobby to start")
        kotlinx.coroutines.delay(5000)
        waiting = false
        Log.d("LobbyScreen", "Lobby has started")
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Players: ${playersCount?.value}")
        LazyColumn {
            playersList?.let { list ->
                items(list.toList()) {
                    Text(it.name)
                }
            }
        }

        if (waiting) {
            ContainedLoadingIndicator()
        } else {
            Text("Lobby has started")
            //TODO: display quiz/question carrousel
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LobbyScreenPreview() {
    LobbyScreen(
        viewModel = null,
        lobby = Lobby("1", "batata_uuid", 1, 120, false)
    )
}