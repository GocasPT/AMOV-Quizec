package pt.isec.amov.quizec.ui.viewmodels

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.isec.amov.quizec.ui.screens.question.manage.ManageQuestionScreen
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

    Scaffold(modifier = modifier.fillMaxSize(), bottomBar = {
        BottomAppBar(contentPadding = PaddingValues(8.dp), content = {
            IconButton(onClick = { navController.navigate("quiz") }) {
                Icon(Icons.AutoMirrored.Filled.List, null)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { navController.navigate("question") }) {
                Icon(Icons.AutoMirrored.Filled.List, null)
            }
            Spacer(modifier = Modifier.weight(1f))
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

                        "manageQuestion" -> {
                            Text("Manage Question")
                        }
                    }
                }
            }
        })
    }) { innerPadding ->
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
                        navController.navigate("manageQuiz")
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
                        navController.navigate("quiz") //There's no recomposition after deleteQuiz so we need to navigate to refresh the screen (Most likely the wrong way to do it)
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
            composable("question") {
                QuestionListScreen(questionList = viewModel.questionList.getQuestionList(),
                    onSelectQuestion = { question ->
                        Log.d("Question selected", question.content)
                        viewModel.selectQuiz(question)
                        navController.navigate("manageQuestion")
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
                        navController.navigate("question") //There's no recomposition after deleteQuestion so we need to navigate to refresh the screen (Most likely the wrong way to do it)
                    }
                )
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