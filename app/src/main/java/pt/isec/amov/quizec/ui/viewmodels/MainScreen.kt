package pt.isec.amov.quizec.ui.viewmodels

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.isec.amov.quizec.model.Lobby
import pt.isec.amov.quizec.model.User
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.ui.screens.HomeScreen
import pt.isec.amov.quizec.ui.screens.auth.BottomNavBar
import pt.isec.amov.quizec.ui.screens.lobby.LobbyScreen
import pt.isec.amov.quizec.ui.screens.question.QuestionListScreen
import pt.isec.amov.quizec.ui.screens.question.QuestionShowScreen
import pt.isec.amov.quizec.ui.screens.question.manage.ManageQuestionScreen
import pt.isec.amov.quizec.ui.screens.quiz.QuizListScreen
import pt.isec.amov.quizec.ui.screens.quiz.QuizShowScreen
import pt.isec.amov.quizec.ui.screens.quiz.manage.ManageQuizScreen

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

    data object Logout :
        BottomNavBarItem(
            "Logout",
            Icons.AutoMirrored.Filled.Logout
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
        BottomNavBarItem.Logout
    )

    //TODO: viewModel get the data and the screen wait until receive data
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            viewModel.dbClient
                .from("quiz")
                .select {
                    filter { eq("owner", user!!.id) }
                }
                .decodeList<Quiz>().let { quizList ->
                    quizList.forEach { quiz ->
                        val questions = viewModel.dbClient
                            .from("question")
                            .select(Columns.raw("*, quiz_question!inner(*)")) {
                                filter {
                                    eq("quiz_question.quiz_id", quiz.id!!)
                                }
                            }
                            .decodeList<Question>()

                        quiz.questions = questions
                        viewModel.quizList.addQuiz(quiz)
                    }
                }
        }

        withContext(Dispatchers.IO) {
            viewModel.dbClient
                .from("question")
                .select {
                    filter {
                        eq("user_id", user!!.id)
                    }
                }
                .decodeList<Question>().let { list ->
                    list.forEach {
                        viewModel.questionList.addQuestion(it)
                    }
                }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(
                items = items,
                currentScreen = currentScreen?.destination?.route,
                onItemSelected = { selected ->
                    when (selected.title) {
                        "Home" -> navController.navigate("home")
                        "Quiz" -> navController.navigate("quiz")
                        "Question" -> navController.navigate("question")
                        "History" -> navController.navigate("history")
                        "Logout" -> onSignOut()
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            startDestination = "home",
            navController = navController,
            modifier = modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    onJoinLobby = { code ->
                        viewModel.joinLobby(code)
                        navController.navigate("lobby")
                    },
                    onCreateLobby = {
                        /* TODO: create lobby
                            - go to quiz list
                            - select quiz
                            - viewModel.createLobby(quizId, ...)
                        */
                    }
                )
            }
            composable("lobby") {
                LobbyScreen(
                    viewModel = viewModel,
                    lobby = viewModel.currentLobby ?: Lobby(
                        "123465",
                        "321654",
                        1,
                        120
                    ), //TODO: fix this
                )
            }
            composable("quiz") {
                QuizListScreen(
                    quizList = viewModel.quizList.getQuizList(),
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
                    }
                )
            }
            composable("show-quiz") {
                viewModel.currentQuiz?.let {
                    Log.d("Quiz selected", viewModel.currentQuiz!!.title)
                    QuizShowScreen(
                        quiz = viewModel.currentQuiz!!,
                        onCreateLobby = { code ->
                            viewModel.createLobby(
                                code,
                                120
                            ) //TODO: receive all parameters to config lobby
                        },
                        onCreateQuestion = { /* TODO */ }
                    )
                }
            }
            composable("manageQuiz") {
                ManageQuizScreen(
                    quiz = viewModel.currentQuiz,
                    userId = user!!.id,
                    questionList = viewModel.questionList.getQuestionList(),
                    saveQuiz = { quiz ->
                        viewModel.saveQuiz(quiz)
                        navController.navigate("quiz")
                    }
                )
            }
            composable("question") {
                QuestionListScreen(questionList = viewModel.questionList.getQuestionList(),
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
                    }
                )
            }

            composable("show-question") {
                viewModel.currentQuestion?.let {
                    Log.d("Question selected", viewModel.currentQuestion!!.content)
                    QuestionShowScreen(question = viewModel.currentQuestion!!)
                }
            }

            composable("manageQuestion") {
                ManageQuestionScreen(
                    question = viewModel.currentQuestion,
                    userId = user!!.id,
                    saveQuestion = { question ->
                        viewModel.saveQuestion(question)
                        navController.navigate("question")
                    }
                )
            }
        }
    }
}