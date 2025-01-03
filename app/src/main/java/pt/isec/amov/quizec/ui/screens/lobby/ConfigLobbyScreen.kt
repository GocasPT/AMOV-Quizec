package pt.isec.amov.quizec.ui.screens.lobby

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isec.amov.quizec.model.User

@Composable
fun ConfigLobbyScreen(
    owner: User,
    onCreateLobby: (/* TODO: add all params to setup the lobby */) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Text("ConfigLobbyScreen")
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigLobbyScreenPreview() {
    ConfigLobbyScreen(
        owner = User("1", "batata_uuid", "batata"),
        onCreateLobby = {}
    )
}