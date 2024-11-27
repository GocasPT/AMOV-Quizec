package pt.isec.amov.quizec.ui.viewmodels

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pt.isec.amov.quizec.ui.screens.QuestionListScreen
import pt.isec.amov.quizec.ui.screens.QuizListScreen

@Composable
fun MainScree(
    viewModel: QuizecViewModel,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
        ) {
            QuestionListScreen(
                questionList = viewModel.questionList.getQuestionList(),
                onSelectQuestion = { question ->
                    Log.d("Question selected", question.title)
                }
            )
            QuizListScreen(
                quizList = viewModel.quizList.getQuizList(),
                onSelectQuiz = { quiz ->
                    Log.d("Quiz selected", quiz.title)
                }
            )
        }
    }
}