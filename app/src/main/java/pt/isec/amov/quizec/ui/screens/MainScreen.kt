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

sealed class BottomNavBarItem(
    var title: String,
    var icon: ImageVector
) {
    data object Home :
        BottomNavBarItem(
            "Home",
            Icons.Filled.Home
        )

    data object Quiz :
        BottomNavBarItem(
            "Quiz",
            Icons.Filled.ContentPaste
        )

    data object Question :
        BottomNavBarItem(
            "Question",
            Icons.Filled.Checklist
        )

    data object History :
        BottomNavBarItem(
            "History",
            Icons.Filled.History
        )

    data object Settings :
        BottomNavBarItem(
            "Settings",
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

    //?TODO: can be improved?
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
                        "Home" -> navController.navigate("Home")
                        "Quiz" -> navController.navigate("Quiz")
                        "Question" -> navController.navigate("Question")
                        "History" -> navController.navigate("History")
                        "Settings" -> navController.navigate("Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            startDestination = "Home",
            navController = navController,
            modifier = modifier.padding(innerPadding)
        ) {
            composable("Home") {
                HomeScreen(
                    viewModel = viewModel,
                    username = user!!.username,
                    onJoinLobby = { code ->
                        viewModel.joinLobby(code)
                        navController.navigate("lobby")
                    },
                    onCreateLobby = {
                        navController.navigate("setup-lobby")
                    }
                )
            }
            composable("setup-lobby") {
                ManageLobbyScreen(
                    viewModel = viewModel,
                    isNewLobby = viewModel.currentLobby.value == null,
                    onCreateLobby = { quizId, started, localRestricted, duration ->
                        viewModel.createLobby(quizId, started, localRestricted, duration)
                        navController.navigate("Home")
                    },
                    //onBack = { navController.popBackStack() }
                )
            }
            composable("lobby") {
                LobbyScreen(
                    viewModel = viewModel,
                )
            }
            composable("Quiz") {
                QuizListScreen(
                    quizList = viewModel.quizList,
                    onSelectQuiz = { quiz ->
                        Log.d("Quiz selected", quiz.title)
                        viewModel.selectQuiz(quiz)
                        navController.navigate("show-quiz")
                    },
                    onCreateQuiz = {
                        viewModel.createQuiz()
                        navController.navigate("manageQuiz")
                    },
                    onEditQuiz = { quiz ->
                        viewModel.selectQuiz(quiz)
                        navController.navigate("manageQuiz")
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
            composable("show-quiz") {
                viewModel.currentQuiz.let {
                    Log.d("Quiz selected", viewModel.currentQuiz.value!!.title)
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
                            navController.navigate("manageQuiz")
                        },
                        onCreateLobby = { code ->
                            viewModel.quizList.find { it.id == code.toInt() }
                                ?.let { it1 -> viewModel.selectQuiz(it1) }
                            navController.navigate("setup-lobby")
                        },
                    )
                }
            }
            composable("manageQuiz") {
                ManageQuizScreen(
                    quiz = viewModel.currentQuiz.value,
                    userId = user!!.id,
                    questionList = viewModel.questionList,
                    saveQuiz = { quiz ->
                        viewModel.saveQuiz(quiz)
                        navController.navigate("quiz")
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable("Question") {
                QuestionListScreen(
                    questionList = viewModel.questionList,
                    onSelectQuestion = { question ->
                        Log.d("Question selected", question.content)
                        viewModel.selectQuestion(question)
                        navController.navigate("show-question")
                    },
                    onCreateQuestion = {
                        viewModel.createQuestion()
                        navController.navigate("manageQuestion")
                    },
                    onEditQuestion = { question ->
                        viewModel.selectQuestion(question)
                        navController.navigate("manageQuestion")
                    },
                    onDeleteQuestion = { question ->
                        viewModel.deleteQuestion(question)
                    },
                    onDuplicateQuestion = { question ->
                        viewModel.duplicateQuestion(question)
                    }
                )
            }

            composable("show-question") {
                viewModel.currentQuestion.let {
                    Log.d("Question selected", viewModel.currentQuestion.value!!.content)
                    QuestionShowScreen(
                        question = viewModel.currentQuestion.value!!,
                        onBack = { navController.popBackStack() },
                        onEdit = { question ->
                            viewModel.selectQuestion(question)
                            navController.navigate("manageQuestion")
                        }
                    )
                }
            }

            composable("manageQuestion") {
                ManageQuestionScreen(
                    question = viewModel.currentQuestion.value,
                    userId = user!!.id,
                    saveQuestion = { question ->
                        viewModel.saveQuestion(question)
                        navController.navigate("question")
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("History") {
                QuizHistoryScreen(
                    onSelectHistory = { history ->
                        viewModel.selectHistory(history)
                        navController.navigate("show-history")
                    },
                    historyList = viewModel.historyList,
                )
            }

            composable("show-history") {
                viewModel.currentHistory.let {
                    HistoryShowScreen(history = viewModel.currentHistory.value!!)
                }
            }

            composable("Settings") {
                SettingsScreen(
                    onSignOut = onSignOut,
                    onCredits = {
                        navController.navigate("credits")
                    }
                )
            }

            composable("credits") {
                CreditsScreen(
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}