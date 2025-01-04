package pt.isec.amov.quizec.ui.screens.settings

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SettingsScreen(
    onSignOut: () -> Unit,
) {
//    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)
//        SettingsScreenLandscape()
//    else
//        SettingsScreenPortrait()

    Button(
        onClick = onSignOut
    ) {
        Text("Sign Out")
    }
}
