package pt.isec.amov.quizec.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.launch
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.User
import pt.isec.amov.quizec.utils.SAuthUtil
import pt.isec.amov.quizec.utils.Strings

fun UserInfo.toUser(): User {
    val id = this.id
    val displayName = this.userMetadata.toString()
    val strEmail = this.email ?: Strings.get(R.string.n_a)
    return User(id, displayName, strEmail)
}

class QuizecAuthViewModel(private val dbClient : SupabaseClient) : ViewModel() {
    private val _user = mutableStateOf(dbClient.auth.currentUserOrNull()?.toUser())
    val user: MutableState<User?>
        get() = _user

    private val _error = mutableStateOf<String?>(null)
    val error: MutableState<String?>
        get() = _error

    fun signUpWithEmail(email: String, password: String, repeatedPassword : String, name: String) {
        if (email.isBlank() || password.isBlank() || name.isBlank() || repeatedPassword.isBlank()) {
            _error.value = Strings.get(R.string.please_fill_in_the_blanks)
            return
        }

        if (password != repeatedPassword) {
            _error.value = Strings.get(R.string.passwords_do_not_match)
            return
        }

        viewModelScope.launch {
            try {
                SAuthUtil.signUpWithEmail(email, password, name)
                _error.value = Strings.get(R.string.success)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _error.value = Strings.get(R.string.please_fill_in_the_blanks)
            return
        }

        viewModelScope.launch {
            try {
                SAuthUtil.signInWithEmail(email, password)
                _user.value = SAuthUtil.currentUser?.toUser()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            SAuthUtil.signOut()
            _user.value = null
            _error.value = null
        }
    }

    fun clearError() {
        _error.value = null
    }
}