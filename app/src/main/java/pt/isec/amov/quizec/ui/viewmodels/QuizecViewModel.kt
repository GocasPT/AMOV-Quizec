package pt.isec.amov.quizec.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionList
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.model.quiz.QuizList
import kotlin.random.Random

class QuizecViewModel(val dbClient: SupabaseClient) : ViewModel() {
    //TODO: PLACE_HOLDER
    val questionList: QuestionList = QuestionList()
    val quizList: QuizList = QuizList()

    //TODO: add data variables
    private var _currentQuiz = mutableStateOf<Quiz?>(null)
    private var _currentQuestion = mutableStateOf<Question?>(null)
    val currentQuiz: Quiz? get() = _currentQuiz.value
    val currentQuestion: Question? get() = _currentQuestion.value

    fun createQuestion() {
        _currentQuestion.value = null
    }

    fun selectQuestion(question: Question) {
        _currentQuestion.value = question
    }

    fun saveQuestion(question: Question) {
        if (_currentQuestion.value != null) {
            questionList.updateQuestion(question)
        } else {
            questionList.addQuestion(question)
        }
        _currentQuestion.value = null
    }

    fun deleteQuestion(question: Question) {
        questionList.removeQuestion(question)
    }

    fun createQuiz() {
        _currentQuiz.value = null
    }

    fun selectQuiz(quiz: Quiz) {
        _currentQuiz.value = quiz
    }

    fun saveQuiz(quiz: Quiz) {
        if (_currentQuiz.value != null) {
            quizList.updateQuiz(quiz)
        } else {
            quizList.addQuiz(quiz)
        }
        _currentQuiz.value = null
    }

    fun deleteQuiz(quiz: Quiz) {
        quizList.removeQuiz(quiz)
    }

    //!PLACE_HOLDER
    fun createLobby() {
        viewModelScope.launch {
            try {
                dbClient.auth.signInWith(Email) {
                    email = "batata@gmail.com"
                    password = "1234"
                }

                val currentUser = dbClient.auth.currentUserOrNull()
                    ?: throw Exception("User not logged in")

                val lobbyCode = Random.nextInt(1000, 9999)
                val quizId: Long = 1

                val lobby = hashMapOf(
                    "lobby_code" to "$lobbyCode",
                    "user_id" to currentUser.id,
                    "quiz_id" to "$quizId"
                )

                dbClient.from("lobby").insert(lobby) { select() }
            } catch (e: Exception) {
                Log.e("QuizecViewModel", "createLobby: ${e.message}")
            }
        }
    }

    //!PLACE_HOLDER
    fun joinLobby(lobbyCode: Int) {
        viewModelScope.launch {
            try {
                dbClient.auth.signInWith(Email) {
                    email = "quizec@isec.pt"
                    password = "quizec"
                }

                val currentUser = dbClient.auth.currentUserOrNull()
                    ?: throw Exception("User not logged in")

                val lobby = dbClient.from("lobby").select(
                    columns = Columns.list("id, lobby_code")
                ) {
                    filter {
                        eq("lobby_code", lobbyCode)
                    }
                }.decodeSingleOrNull<Map<String, String>>() ?: throw Exception("Lobby not found")

                dbClient.from("lobby_users").insert(
                    hashMapOf(
                        "lobby_id" to lobby["id"],
                        "user_id" to currentUser.id //TODO: user_id OR user_email?
                    )
                )
            } catch (e: Exception) {
                Log.e("QuizecViewModel", "joinLobby: ${e.message}")
            }
        }
    }
}