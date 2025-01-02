package pt.isec.amov.quizec.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.launch
import pt.isec.amov.quizec.model.User
import pt.isec.amov.quizec.utils.SAuthUtil

fun UserInfo.toUser(): User {
    val displayName = this.userMetadata.toString()
    val strEmail = this.email ?: "n.d."
    return User(displayName, strEmail)
}

class QuizecAuthViewModel(val dbClient: SupabaseClient) : ViewModel() {
    private val _user = mutableStateOf(dbClient.auth.currentUserOrNull()?.toUser())
    val user: MutableState<User?>
        get() = _user

    private val _error = mutableStateOf<String?>(null)
    val error: MutableState<String?>
        get() = _error

    fun createUserWithEmail(email: String, password: String, name: String) {
        if (email.isBlank() || password.isBlank() || name.isBlank())
            return

        viewModelScope.launch {
            try {
                SAuthUtil.createUserWithEmail(email, password)
                _user.value = SAuthUtil.currentUser?.toUser()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank())
            return
        viewModelScope.launch {
            try {
                SAuthUtil.signInWithEmail(email, password)
                _user.value = SAuthUtil.currentUser?.toUser()
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

}