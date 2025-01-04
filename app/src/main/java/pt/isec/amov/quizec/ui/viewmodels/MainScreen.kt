package pt.isec.amov.quizec.ui.viewmodels

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
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.User
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.ui.screens.HomeScreen
import pt.isec.amov.quizec.ui.screens.QuestionListScreen
import pt.isec.amov.quizec.ui.screens.auth.BottomNavBar
import pt.isec.amov.quizec.ui.screens.credits.CreditsScreen
import pt.isec.amov.quizec.ui.screens.history.HistoryShowScreen
import pt.isec.amov.quizec.ui.screens.question.QuestionShowScreen
import pt.isec.amov.quizec.ui.screens.question.manage.ManageQuestionScreen
import pt.isec.amov.quizec.ui.screens.history.QuizHistoryScreen
import pt.isec.amov.quizec.ui.screens.quiz.QuizListScreen
import pt.isec.amov.quizec.ui.screens.quiz.QuizShowScreen
import pt.isec.amov.quizec.ui.screens.quiz.manage.ManageQuizScreen
import pt.isec.amov.quizec.ui.screens.settings.SettingsScreen

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

    val items = listOf(
        BottomNavBarItem.Home,
        BottomNavBarItem.Quiz,
        BottomNavBarItem.Question,
        BottomNavBarItem.History,
        BottomNavBarItem.Settings
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
                .select() {
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
                    username = user!!.name,
                )
            }
            composable("Quiz") {
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
                        onBack = { navController.popBackStack() },
                        onEdit = { quiz ->
                            viewModel.selectQuiz(quiz)
                            navController.navigate("manageQuiz")
                        }
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
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable("Question") {
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
                    QuestionShowScreen(
                        question = viewModel.currentQuestion!!,
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
                    question = viewModel.currentQuestion,
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
                    onLoad = {
                        viewModel.getHistory(user!!.id)
                    },
                    onCreateDummy = {
                        viewModel.createDummyHistory(user!!.id)
                    },
                    onSelectHistory = { history ->
                        viewModel.selectHistory(history)
                        navController.navigate("show-history")
                    },
                    historyList = viewModel.historyList
                )
            }

            composable("show-history") {
                viewModel.currentHistory?.let {
                    HistoryShowScreen(history = viewModel.currentHistory!!)
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