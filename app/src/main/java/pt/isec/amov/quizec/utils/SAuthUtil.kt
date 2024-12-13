package pt.isec.amov.quizec.utils

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

class SAuthUtil {
    companion object {
        //TODO: how to share the dbClient between classes?
        private val _dbClient: SupabaseClient by lazy {
            createSupabaseClient(
                supabaseUrl = "https://vivtabksraqgsyzulwgz.supabase.co",
                supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZpdnRhYmtzcmFxZ3N5enVsd2d6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzM4NzIyMTEsImV4cCI6MjA0OTQ0ODIxMX0.ZH3W7Ar1L7HGAmifvFdkJ7UuQiVRrNk8ndS5viBfKrc"
            ) {
                install(Auth)
                install(Storage)
                install(Realtime)
                install(Postgrest)
            }
        }

        private val auth by lazy { _dbClient.auth }

        val currentUser: UserInfo?
            get() = auth.currentUserOrNull()

        suspend fun createUserWithEmail(email: String, password: String) {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
        }

        suspend fun signInWithEmail(email: String, password: String) {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }

        suspend fun signOut() {
            if (auth.currentUserOrNull() != null) {
                auth.signOut()
            }
        }
    }
}