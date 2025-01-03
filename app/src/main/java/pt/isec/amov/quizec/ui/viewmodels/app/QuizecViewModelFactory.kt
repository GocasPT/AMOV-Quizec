package pt.isec.amov.quizec.ui.viewmodels.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.jan.supabase.SupabaseClient

class QuizecViewModelFactory(val dbClient: SupabaseClient) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuizecViewModel(dbClient) as T
    }
}