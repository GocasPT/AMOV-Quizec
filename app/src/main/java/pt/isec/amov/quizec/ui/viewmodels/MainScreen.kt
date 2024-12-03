package pt.isec.amov.quizec.ui.viewmodels

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.isec.amov.quizec.ui.screens.question.create.CreateQuestionScreen
import pt.isec.amov.quizec.ui.screens.quiz.manage.ManageQuizScreen
import pt.isec.amov.quizec.ui.screens.question.QuestionListScreen
import pt.isec.amov.quizec.ui.screens.quiz.QuizListScreen

@Composable
fun MainScreen(
    viewModel: QuizecViewModel,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val currentScreen by navController.currentBackStackEntryAsState()
    navController.addOnDestinationChangedListener { _, destination, _ ->
        Log.d("Destination changed", destination.route.toString())
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        bottomBar = {
            BottomAppBar(
                containerColor = BottomAppBarDefaults.containerColor,
                contentColor = Color.Black,
                content = {
                    IconButton(onClick = { navController.navigate("quiz") }) {
                        Icon(Icons.AutoMirrored.Filled.List, null)
                    }
                    IconButton(onClick = { navController.navigate("question") }) {
                        Icon(Icons.AutoMirrored.Filled.List, null)
                    }
                    when (currentScreen?.destination?.route) {
                        "quiz" -> {
                            Text("Quiz")
                        }

                        "question" -> {
                            Text("Question")
                        }

                        "manageQuiz" -> {
                            Text("Manage Quiz")
                        }

                        "createQuestion" -> {
                            Text("Create Question")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            startDestination = "quiz",
            navController = navController,
            modifier = modifier
                .padding(innerPadding)
        ) {
            composable("quiz") {
                QuizListScreen(
                    quizList = viewModel.quizList.getQuizList(),
                    onSelectQuiz = { quiz ->
                        Log.d("Quiz selected", quiz.title)
                    },
                    onCreateQuiz = {
                        viewModel.createQuiz()
                        navController.navigate("manageQuiz")
                    },
                    onEditQuiz = { quiz ->
                        viewModel.selectQuiz(quiz)
                        navController.navigate("manageQuiz")
                    }
                )
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
            composable("createQuestion") {
                CreateQuestionScreen(
                    saveQuestion = { question ->
                        viewModel.saveQuestion(question)
                        navController.navigate("question")
                    }
                )
            }
            composable("question") {
                QuestionListScreen(
                    questionList = viewModel.questionList.getQuestionList(),
                    onSelectQuestion = { question ->
                        Log.d("Question selected", question.content)
                    },
                    onCreateQuestion = { navController.navigate("createQuestion") }
                )
            }
        }
    }
}