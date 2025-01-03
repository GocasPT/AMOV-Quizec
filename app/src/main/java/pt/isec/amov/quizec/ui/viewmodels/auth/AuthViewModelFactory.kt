package pt.isec.amov.quizec.ui.viewmodels.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.jan.supabase.SupabaseClient

class AuthViewModelFactory(val dbClient: SupabaseClient) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(dbClient) as T
    }
}