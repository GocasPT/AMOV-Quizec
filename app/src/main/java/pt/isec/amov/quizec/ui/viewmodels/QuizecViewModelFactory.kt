package pt.isec.amov.quizec.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class QuizecViewModelFactory(/*TODO: receive data*/) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuizecViewModel() as T
    }
}