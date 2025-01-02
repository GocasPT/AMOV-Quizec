package pt.isec.amov.quizec.utils

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import pt.isec.amov.quizec.QuizecApp

class SAuthUtil {
    companion object {
        private val _dbClient get() = QuizecApp.getInstance().dbClient
        private val _auth by lazy { _dbClient.auth }

        val currentUser: UserInfo?
            get() = _auth.currentUserOrNull()

        suspend fun signUpWithEmail(email: String, password: String, username: String) {
            _auth.signUpWith(Email) {
                this.email = email
                this.password = password
                //TODO: check this later
                data = buildJsonObject {
                    put("username", username)
                }
            }
        }

        suspend fun signInWithEmail(email: String, password: String) {
            _auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }

        suspend fun signOut() {
            if (_auth.currentUserOrNull() != null) {
                _auth.signOut()
            }
        }
    }
}