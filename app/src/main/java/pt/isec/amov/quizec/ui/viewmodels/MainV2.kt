package pt.isec.amov.quizec.ui.viewmodels

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.ui.screens.QuestionListScreen
import pt.isec.amov.quizec.ui.screens.auth.BottomNavBar
import pt.isec.amov.quizec.ui.screens.question.QuestionShowScreen
import pt.isec.amov.quizec.ui.screens.question.manage.ManageQuestionScreen
import pt.isec.amov.quizec.ui.screens.quiz.QuizListScreen
import pt.isec.amov.quizec.ui.screens.quiz.QuizShowScreen
import pt.isec.amov.quizec.ui.screens.quiz.manage.ManageQuizScreen

sealed class BottomNavBarItem(
    var title: String,
    var icon: ImageVector
) {
    object Home :
        BottomNavBarItem(
            "Home",
            Icons.Filled.Home
        )

    object Quiz :
        BottomNavBarItem(
            "Quiz",
            Icons.Filled.ContentPaste
        )

    object Question :
        BottomNavBarItem(
            "Question",
            Icons.Filled.Checklist
        )
    object History :
        BottomNavBarItem(
            "History",
            Icons.Filled.History
        )
    object Logout :
        BottomNavBarItem(
            "Logout",
            Icons.AutoMirrored.Filled.Logout
        )
}

@Composable
fun MainV2(
    viewModel: QuizecViewModel,
    navController: NavHostController = rememberNavController(),
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentScreen by navController.currentBackStackEntryAsState()
    var selectedItem by remember { mutableStateOf<BottomNavBarItem>(BottomNavBarItem.Home) }

    val items = listOf(
        BottomNavBarItem.Home,
        BottomNavBarItem.Quiz,
        BottomNavBarItem.Question,
        BottomNavBarItem.History,
        BottomNavBarItem.Logout
    )

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            viewModel.dbClient.from("question").select().decodeList<Question>().let { list ->
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
                        "Home" -> navController.navigate("quiz")
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
            startDestination = "quiz",
            navController = navController,
            modifier = modifier.padding(innerPadding)
        ) {
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
                        navController.navigate("quiz")
                    }
                )
            }
            composable("show-quiz") {
                viewModel.currentQuiz?.let {
                    QuizShowScreen(quiz = viewModel.currentQuiz!!)
                }
            }
            composable("manageQuiz") {
                ManageQuizScreen(
                    quiz = viewModel.currentQuiz,
                    questionList = viewModel.questionList.getQuestionList(),
                    saveQuiz = { quiz ->
                        viewModel.saveQuiz(quiz)
                        navController.navigate("quiz")
                    }
                )
            }
            composable("question") {
                QuestionListScreen(
                    questionList = viewModel.questionList.getQuestionList(),
                    onSelectQuestion = { question ->
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
                        navController.navigate("question")
                    }
                )
            }
            composable("show-question") {
                viewModel.currentQuestion?.let {
                    QuestionShowScreen(question = viewModel.currentQuestion!!)
                }
            }
            composable("manageQuestion") {
                ManageQuestionScreen(
                    question = viewModel.currentQuestion,
                    saveQuestion = { question ->
                        viewModel.saveQuestion(question)
                        navController.navigate("question")
                    }
                )
            }
        }
    }
}
