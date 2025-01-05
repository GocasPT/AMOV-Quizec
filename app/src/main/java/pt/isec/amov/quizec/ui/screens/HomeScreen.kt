package pt.isec.amov.quizec.ui.screens

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.Lobby
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.ui.viewmodels.app.QuizecViewModel

@Composable
fun HomeScreen(
    viewModel: QuizecViewModel,
    username: String,
    onJoinLobby: (String) -> Unit,
    onCreateLobby: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)
        HomeScreenLandscape(
            viewModel = viewModel,
            username = username,
            onJoinLobby = onJoinLobby,
            onCreateLobby = onCreateLobby,
            modifier = modifier
        )
    else
        HomeScreenPortrait(
            viewModel = viewModel,
            username = username,
            onJoinLobby = onJoinLobby,
            onCreateLobby = onCreateLobby,
            modifier = modifier
        )
}


@Composable
fun HomeScreenLandscape(
    viewModel: QuizecViewModel,
    username: String,
    onJoinLobby: (String) -> Unit,
    onCreateLobby: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val code = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getLobbies()
    }

    LazyRow(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Image Section
        item {
            Box(
                modifier = Modifier
                    .size(300.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.fundo_exemplo),
                    contentDescription = "Quiz Image",
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Gray.copy(alpha = 0.8f)
                                ),
                                startY = 0f
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = stringResource(R.string.welcome, username),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Code Input Section
        item {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                OutlinedTextField(
                    value = code.value,
                    onValueChange = { if (it.length <= 6) code.value = it },
                    label = { Text(stringResource(R.string.join_quiz)) },
                    textStyle = TextStyle(fontSize = 28.sp),
                    singleLine = true,
                    shape = RoundedCornerShape(percent = 20),
                    trailingIcon = {
                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            onClick = { onJoinLobby(code.value) },
                            shape = CircleShape
                        ) {
                            Icon(Icons.Filled.Check, contentDescription = "Join button")
                        }
                    }
                )
            }
        }

        // Create Room Section
        item {
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                ElevatedButton(
                    onClick = onCreateLobby,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(stringResource(R.string.create_room))
                }
            }
        }

        // My Rooms Section
        item {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.my_rooms),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(viewModel.currentLobbiesList) { lobby ->
                        Log.d("HomeScreen", "lobby: $lobby")
                        QuizLobbyCard(
                            lobby = lobby,
                            onSelectQuiz = {},
                            people = 10
                        )
                    }
                }
            }
        }
    }

}


@Composable
fun HomeScreenPortrait(
    viewModel: QuizecViewModel,
    username: String,
    onJoinLobby: (String) -> Unit,
    onCreateLobby: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val code = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getLobbies()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.fundo_exemplo),
                    contentDescription = "Quiz Image",
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Gray.copy(alpha = 0.8f)
                                ),
                                startY = 0f
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = stringResource(R.string.welcome, username),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    OutlinedTextField(
                        value = code.value,
                        onValueChange = { if (it.length <= 6) code.value = it },
                        label = { Text(stringResource(R.string.join_quiz)) },
                        textStyle = TextStyle(fontSize = 28.sp),
                        singleLine = true,
                        shape = RoundedCornerShape(percent = 20),
                        trailingIcon = {
                            FloatingActionButton(
                                modifier = Modifier.padding(16.dp),
                                onClick = { onJoinLobby(code.value) },
                                shape = CircleShape
                            ) {
                                Icon(Icons.Filled.Check, contentDescription = "Floating action button.")
                            }
                        }
                    )

                    HorizontalDivider(
                        color = Color.Gray,
                        thickness = 2.dp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    ElevatedButton(
                        onClick = onCreateLobby,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(stringResource(R.string.create_room))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = stringResource(R.string.my_rooms),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(viewModel.currentLobbiesList) { lobby ->
                        Log.d("HomeScreen", "lobby: $lobby")
                        QuizLobbyCard(
                            lobby = lobby,
                            onSelectQuiz = {},
                            people = 10
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun QuizLobbyCard(
    lobby: Lobby,
    onSelectQuiz: (Quiz) -> Unit,
    people: Int,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {  },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFBDA9A9),
        )
    ) {
        Box(
            modifier = Modifier
                .height(180.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = lobby.code,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = if (lobby.started) "LIVE" else "WAITING",
                        color = if (lobby.started) Color.Red else Color(0xFF36AD36),
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "${lobby.expiredAt} | ${lobby.duration}:00",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = if (lobby.localRestricted) "Restricted" else "Public",
                        fontSize = 12.sp,
                        color = if (lobby.started) Color.Red else Color(0xFF36AD36),
                        )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.People,
                            contentDescription = "People",
                            tint = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = people.toString(),
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}
