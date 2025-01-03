package pt.isec.amov.quizec.ui.screens.lobby

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isec.amov.quizec.model.Lobby

@Composable
fun OwnerLobbyScreen(
    lobby: Lobby,
    modifier: Modifier = Modifier,
) {
    var playersCount by remember { mutableIntStateOf(0) }

    //TODO: add real-time player count
    LaunchedEffect(Unit) {
        //SRealTimeUtil.getPlayerCounterFlow("59HBGQ").collect { playersCount = it }
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Text("Lobby: ${lobby.code}")
        Text("Players: $playersCount")
    }
}

@Preview(showBackground = true)
@Composable
fun OwnerLobbyScreenPreview() {
    OwnerLobbyScreen(
        lobby = Lobby("1", "batata_uuid", 1, 120, false)
    )
}