package pt.isec.amov.quizec.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.User
import pt.isec.amov.quizec.ui.screens.auth.BottomNavBar
import pt.isec.amov.quizec.ui.screens.credits.CreditsScreen
import pt.isec.amov.quizec.ui.screens.history.HistoryShowScreen
import pt.isec.amov.quizec.ui.screens.history.QuizHistoryScreen
import pt.isec.amov.quizec.ui.screens.lobby.LobbyScreen
import pt.isec.amov.quizec.ui.screens.lobby.ManageLobbyScreen
import pt.isec.amov.quizec.ui.screens.question.QuestionListScreen
import pt.isec.amov.quizec.ui.screens.question.QuestionShowScreen
import pt.isec.amov.quizec.ui.screens.question.manage.ManageQuestionScreen
import pt.isec.amov.quizec.ui.screens.quiz.QuizListScreen
import pt.isec.amov.quizec.ui.screens.quiz.QuizShowScreen
import pt.isec.amov.quizec.ui.screens.quiz.manage.ManageQuizScreen
import pt.isec.amov.quizec.ui.screens.settings.SettingsScreen
import pt.isec.amov.quizec.ui.viewmodels.app.QuizecViewModel
import pt.isec.amov.quizec.utils.Strings

sealed class BottomNavBarItem(
    var title: String,
    var icon: ImageVector
) {
    data object Home :
        BottomNavBarItem(
            Strings.get(R.string.homeNav),
            Icons.Filled.Home
        )

    data object Quiz :
        BottomNavBarItem(
            Strings.get(R.string.quizNav),
            Icons.Filled.ContentPaste
        )

    data object Question :
        BottomNavBarItem(
            Strings.get(R.string.questionNav),
            Icons.Filled.Checklist
        )

    data object History :
        BottomNavBarItem(
            Strings.get(R.string.historyNav),
            Icons.Filled.History
        )

    data object Settings :
        BottomNavBarItem(
            Strings.get(R.string.settingsNav),
            Icons.Filled.Settings
        )
}


@Composable
fun MainScreen(
    viewModel: QuizecViewModel,
    user: User?,
    navController: NavHostController = rememberNavController(),
    onSignOut: () -> Unit = {},
    modifier: Modifier = Modifier,
) { 
    val currentScreen by navController.currentBackStackEntryAsState()
    navController.addOnDestinationChangedListener { _, destination, _ ->
        Log.d("Destination changed", destination.route.toString())
    }

    val items = listOf(
        BottomNavBarItem.Home,
        BottomNavBarItem.Quiz,
        BottomNavBarItem.Question,
        BottomNavBarItem.History,
        BottomNavBarItem.Settings
    )


    LaunchedEffect(Unit) {
        viewModel.clearData()
        viewModel.fetchData()
    }

    Scaffold(
        containerColor = colorResource(id = R.color.defaultBackground),
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(
                items = items,
                currentScreen = currentScreen?.destination?.route,
                onItemSelected = { selected ->
                    when (selected.title) {
                        Strings.get(R.string.homeNav) -> navController.navigate(Strings.get(R.string.homeNav))
                        Strings.get(R.string.quizNav) -> navController.navigate(Strings.get(R.string.quizNav))
                        Strings.get(R.string.questionNav) -> navController.navigate(Strings.get(R.string.questionNav))
                        Strings.get(R.string.historyNav) -> navController.navigate(Strings.get(R.string.historyNav))
                        Strings.get(R.string.settingsNav) -> navController.navigate(Strings.get(R.string.settingsNav))
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            startDestination = Strings.get(R.string.homeNav),
            navController = navController,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Strings.get(R.string.homeNav)) {
                HomeScreen(
                    viewModel = viewModel,
                    username = user!!.username,
                    onJoinLobby = { code ->
                        viewModel.joinLobby(code)
                        navController.navigate(Strings.get(R.string.lobby))
                    },
                    onCreateLobby = {
                        navController.navigate(Strings.get(R.string.setupLobby))
                    }
                )
            }
            composable(Strings.get(R.string.setupLobby)) {
                ManageLobbyScreen(
                    viewModel = viewModel,
                    isNewLobby = viewModel.currentLobby.value == null,
                    onCreateLobby = { quizId, started, localRestricted, duration ->
                        viewModel.createLobby(quizId, started, localRestricted, duration)
                        navController.navigate(Strings.get(R.string.homeNav))
                    },
                    onStopLobby = {
                        viewModel.stopLobby()
                        navController.navigate("Home")
                    }
                    //onBack = { navController.popBackStack() }
                )
            }
            composable(Strings.get(R.string.lobby)) {
                LobbyScreen(
                    viewModel = viewModel,
                )
            }
            composable(Strings.get(R.string.quizNav)) {
                QuizListScreen(
                    quizList = viewModel.quizList,
                    onSelectQuiz = { quiz ->
                        viewModel.selectQuiz(quiz)
                        navController.navigate(Strings.get(R.string.showQuiz))
                    },
                    onCreateQuiz = {
                        viewModel.createQuiz()
                        navController.navigate(Strings.get(R.string.manageQuiz))
                    },
                    onEditQuiz = { quiz ->
                        viewModel.selectQuiz(quiz)
                        navController.navigate(Strings.get(R.string.manageQuiz))
                    },
                    onDeleteQuiz = { quiz ->
                        viewModel.deleteQuiz(quiz)
                    },
                    onSearch = {},
                    onFilter = {},
                    onDuplicateQuiz = { quiz ->
                        viewModel.duplicateQuiz(quiz)
                    }
                )
            }
            composable(Strings.get(R.string.showQuiz)) {
                viewModel.currentQuiz.let {
                    QuizShowScreen(
                        quiz = viewModel.currentQuiz.value!!,
                        questionList = viewModel.questionList,
                        onBack = { navController.popBackStack() },
                        onCreateQuestion = { //!TODO
                            /*viewModel.selectQuiz(null)
                                        navController.navigate("manageQuiz")*/
                        },
                        onEdit = { quiz ->
                            viewModel.selectQuiz(quiz)
                            navController.navigate(Strings.get(R.string.manageQuiz))
                        },
                        onCreateLobby = { code ->
                            viewModel.quizList.find { it.id == code.toInt() }
                                ?.let { it1 -> viewModel.selectQuiz(it1) }
                            navController.navigate(Strings.get(R.string.setupLobby))
                        },
                    )
                }
            }
            composable(Strings.get(R.string.manageQuiz)) {
                ManageQuizScreen(
                    quiz = viewModel.currentQuiz.value,
                    userId = user!!.id,
                    questionList = viewModel.questionList,
                    saveQuiz = { quiz ->
                        viewModel.saveQuiz(quiz)
                        navController.navigate(Strings.get(R.string.quizNav))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Strings.get(R.string.questionNav)) {
                QuestionListScreen(
                    questionList = viewModel.questionList,
                    onSelectQuestion = { question ->
                        viewModel.selectQuestion(question)
                        navController.navigate(Strings.get(R.string.showQuestion))
                    },
                    onCreateQuestion = {
                        viewModel.createQuestion()
                        navController.navigate(Strings.get(R.string.manageQuestion))
                    },
                    onEditQuestion = { question ->
                        viewModel.selectQuestion(question)
                        navController.navigate(Strings.get(R.string.manageQuestion))
                    },
                    onDeleteQuestion = { question ->
                        viewModel.deleteQuestion(question)
                    },
                    onDuplicateQuestion = { question ->
                        viewModel.duplicateQuestion(question)
                    }
                )
            }

            composable(Strings.get(R.string.showQuestion)) {
                viewModel.currentQuestion.let {
                    Log.d("Question selected", viewModel.currentQuestion.value!!.content)
                    QuestionShowScreen(
                        question = viewModel.currentQuestion.value!!,
                        onBack = { navController.popBackStack() },
                        onEdit = { question ->
                            viewModel.selectQuestion(question)
                            navController.navigate(Strings.get(R.string.manageQuestion))
                        }
                    )
                }
            }

            composable(Strings.get(R.string.manageQuestion)) {
                ManageQuestionScreen(
                    question = viewModel.currentQuestion.value,
                    userId = user!!.id,
                    saveQuestion = { question ->
                        viewModel.saveQuestion(question)
                        navController.navigate(Strings.get(R.string.questionNav))
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Strings.get(R.string.historyNav)) {
                QuizHistoryScreen(
                    onSelectHistory = { history ->
                        viewModel.selectHistory(history)
                        navController.navigate(Strings.get(R.string.showHistory))
                    },
                    historyList = viewModel.historyList,
                )
            }

            composable(Strings.get(R.string.showHistory)) {
                viewModel.currentHistory.let {
                    HistoryShowScreen(history = viewModel.currentHistory.value!!)
                }
            }

            composable(Strings.get(R.string.settingsNav)) {
                SettingsScreen(
                    onSignOut = onSignOut,
                    onCredits = {
                        navController.navigate(Strings.get(R.string.credits))
                    }
                )
            }

            composable(Strings.get(R.string.credits)) {
                CreditsScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}