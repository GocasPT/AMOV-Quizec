package pt.isec.amov.quizec.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isec.amov.quizec.model.Quiz

class QuizecViewModelFactory( val quizList: List<Quiz>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuizecViewModel(quizList) as T
    }
}