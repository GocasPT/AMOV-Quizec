package pt.isec.amov.quizec.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.amov.quizec.model.question.QuestionList
import pt.isec.amov.quizec.model.quiz.QuizList

class QuizecViewModelFactory(val questionList: QuestionList, val quizList: QuizList) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuizecViewModel(questionList, quizList) as T
    }
}