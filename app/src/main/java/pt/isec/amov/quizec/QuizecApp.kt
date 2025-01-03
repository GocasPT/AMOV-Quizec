package pt.isec.amov.quizec

import android.app.Application
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

class QuizecApp : Application() {
    companion object {
        private var instance: QuizecApp? = null

        fun getInstance(): QuizecApp =
            instance ?: throw IllegalStateException("QuizecApp not initialized")
    }

    // Yes, the URL and Key are public safe, doesn't need to be hidden
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

    val dbClient: SupabaseClient
        get() = _dbClient

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}