package pt.isec.amov.quizec

import android.app.Application
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import pt.isec.amov.quizec.model.question.Answer
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionList
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.model.quiz.QuizList
import pt.isec.amov.quizec.utils.QuestionIDGenerator

class QuizecApp : Application() {
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

    //TODO: add local data for testing (no database for now)
    private val _questionList: QuestionList by lazy {
        QuestionList().apply {
            addQuestion(
                Question(
                    QuestionIDGenerator.getNextId(), "Are you insane?", null, Answer.TrueFalse(true)
                )
            )
        }
    }

    private val _quizList: QuizList by lazy {
        QuizList().apply {
            addQuiz(
                Quiz(
                    "XXXXX", "Quiz 0", null, listOf(
                        _questionList.getQuestionList()[0]
                    ), false, 0, false, true
                )
            )
        }
    }

    val quizList: QuizList
        get() = _quizList

    val questionList: QuestionList
        get() = _questionList
}