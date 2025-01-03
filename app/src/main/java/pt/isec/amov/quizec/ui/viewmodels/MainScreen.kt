package pt.isec.amov.quizec.ui.viewmodels

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
import androidx.compose.ui.res.stringResource
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
import pt.isec.amov.quizec.ui.screens.question.QuestionShowScreen
import pt.isec.amov.quizec.ui.screens.question.manage.ManageQuestionScreen
import pt.isec.amov.quizec.ui.screens.quiz.QuizListScreen
import pt.isec.amov.quizec.ui.screens.quiz.QuizShowScreen
import pt.isec.amov.quizec.ui.screens.quiz.manage.ManageQuizScreen

sealed class BottomNavBarItem(
    var title: String,
    var icon: ImageVector
) {
    class Home(title: String) : BottomNavBarItem(title, Icons.Filled.Home)
    class Quiz(title: String) : BottomNavBarItem(title, Icons.Filled.ContentPaste)
    class Question(title: String) : BottomNavBarItem(title, Icons.Filled.Checklist)
    class History(title: String) : BottomNavBarItem(title, Icons.Filled.History)
    class Settings(title: String) : BottomNavBarItem(title, Icons.Filled.Settings)
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

    val homeText = stringResource(id = R.string.home)
    val quizText = stringResource(id = R.string.quiz)
    val questionText = stringResource(id = R.string.question)
    val historyText = stringResource(id = R.string.history)
    val settingsText = stringResource(id = R.string.settings)
    val showQuiz = stringResource(id = R.string.showQuiz)
    val showQuestion = stringResource(id = R.string.showQuestion)
    val manageQuiz = stringResource(id = R.string.managequiz)
    val manageQuestion = stringResource(id = R.string.manageQuestion)

    val items = listOf(
        BottomNavBarItem.Home(title = homeText),
        BottomNavBarItem.Quiz(title = quizText),
        BottomNavBarItem.Question(title = questionText),
        BottomNavBarItem.History(title = historyText),
        BottomNavBarItem.Settings(title = settingsText),
    )

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
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(
                items = items,
                currentScreen = currentScreen?.destination?.route,
                onItemSelected = { selected ->
                    when (selected.title) {
                        homeText -> navController.navigate(homeText)
                        quizText -> navController.navigate(quizText)
                        questionText -> navController.navigate(questionText)
                        historyText -> navController.navigate(historyText)
                        settingsText -> navController.navigate(settingsText)
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            startDestination = homeText,
            navController = navController,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(homeText) {
                HomeScreen()
            }
            composable(quizText) {
                QuizListScreen(
                    quizList = viewModel.quizList.getQuizList(),
                    onSelectQuiz = { quiz ->
                        viewModel.selectQuiz(quiz)
                        navController.navigate(showQuiz)
                    },
                    onCreateQuiz = {
                        viewModel.createQuiz()
                        navController.navigate(manageQuiz)
                    },
                    onEditQuiz = { quiz ->
                        viewModel.selectQuiz(quiz)
                        navController.navigate(manageQuiz)
                    },
                    onDeleteQuiz = { quiz ->
                        viewModel.deleteQuiz(quiz)
                    }
                )
            }
            composable(showQuiz) {
                viewModel.currentQuiz?.let {
                    QuizShowScreen(quiz = viewModel.currentQuiz!!)
                }
            }
            composable(manageQuiz) {
                ManageQuizScreen(
                    quiz = viewModel.currentQuiz,
                    userId = user!!.id,
                    questionList = viewModel.questionList.getQuestionList(),
                    saveQuiz = { quiz ->
                        viewModel.saveQuiz(quiz)
                        navController.navigate(quizText)
                    }
                )
            }
            composable(questionText) {
                QuestionListScreen(questionList = viewModel.questionList.getQuestionList(),
                    onSelectQuestion = { question ->
                        viewModel.selectQuestion(question)
                        navController.navigate(showQuiz)
                    },
                    onCreateQuestion = {
                        viewModel.createQuestion()
                        navController.navigate(manageQuestion)
                    },
                    onEditQuestion = { question ->
                        viewModel.selectQuestion(question)
                        navController.navigate(manageQuestion)
                    },
                    onDeleteQuestion = { question ->
                        viewModel.deleteQuestion(question)
                    }
                )
            }

            composable(showQuestion) {
                viewModel.currentQuestion?.let {
                    QuestionShowScreen(question = viewModel.currentQuestion!!)
                }
            }

            composable(manageQuestion) {
                ManageQuestionScreen(
                    question = viewModel.currentQuestion,
                    userId = user!!.id,
                    saveQuestion = { question ->
                        viewModel.saveQuestion(question)
                        navController.navigate(questionText)
                    }
                )
            }
        }
    }
}