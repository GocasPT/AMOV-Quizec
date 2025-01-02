package pt.isec.amov.quizec.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import pt.isec.amov.quizec.model.Lobby
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionList
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.model.quiz.QuizList
import pt.isec.amov.quizec.utils.CodeGen
import pt.isec.amov.quizec.utils.SStorageUtil

class QuizecViewModel(val dbClient: SupabaseClient) : ViewModel() {
    //TODO: PLACE_HOLDER
    val questionList: QuestionList = QuestionList()
    val quizList: QuizList = QuizList()

    //TODO: add data variables
    private var _currentQuiz = mutableStateOf<Quiz?>(null)
    private var _currentQuestion = mutableStateOf<Question?>(null)
    private var _currentLobby = mutableStateOf<Lobby?>(null)
    val currentQuiz: Quiz? get() = _currentQuiz.value
    val currentQuestion: Question? get() = _currentQuestion.value
    val currentLobby: Lobby? get() = _currentLobby.value

    fun createQuestion() {
        _currentQuestion.value = null
    }

    fun selectQuestion(question: Question) {
        _currentQuestion.value = question
    }

    fun saveQuestion(question: Question) {
        if (_currentQuestion.value != null) {
            viewModelScope.launch {
                try {
                    SStorageUtil.updateQuestionDatabase(dbClient, question) { e ->
                        if (e != null) {
                            Log.d("QuizecViewModel", "Error updating question: $e")
                        } else {
                            questionList.updateQuestion(question)
                        }
                    }
                } catch (e: Throwable) {
                    Log.d("QuizecViewModel", "Error updating question: $e")
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    SStorageUtil.saveQuestionDatabase(dbClient, question) { e, updatedQuestion ->
                        if (e != null) {
                            Log.d("QuizecViewModel", "Error saving question: $e")
                        } else {
                            questionList.addQuestion(updatedQuestion!!)
                        }
                    }
                } catch (e: Throwable) {
                    Log.d("QuizecViewModel", "Error saving question: $e")
                }
            }
        }
        _currentQuestion.value = null
    }

    fun deleteQuestion(question: Question) {
        viewModelScope.launch {
            try {
                SStorageUtil.deleteQuestionDatabase(dbClient, question) { e ->
                    if (e != null) {
                        Log.d("QuizecViewModel", "Error deleting question: $e")
                    } else {
                        questionList.removeQuestion(question)
                    }
                }
            } catch (e: Throwable) {
                Log.d("QuizecViewModel", "Error deleting question: $e")
            }
        }
    }

    fun createQuiz() {
        _currentQuiz.value = null
    }

    fun selectQuiz(quiz: Quiz) {
        _currentQuiz.value = quiz
    }

    fun saveQuiz(quiz: Quiz) {
        if (_currentQuiz.value != null) {
            viewModelScope.launch {
                try {
                    SStorageUtil.updateQuizDatabase(dbClient, quiz) { e ->
                        if (e != null) {
                            Log.d("QuizecViewModel", "Error updating quiz: $e")
                        } else {
                            quizList.updateQuiz(quiz)
                        }
                    }
                } catch (e: Throwable) {
                    Log.d("QuizecViewModel", "Error updating quiz: $e")
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    SStorageUtil.saveQuizDatabase(dbClient, quiz) { e, updatedQuiz ->
                        if (e != null) {
                            Log.d("QuizecViewModel", "Error saving quiz: $e")
                        } else {
                            quizList.addQuiz(updatedQuiz!!)
                        }
                    }
                } catch (e: Throwable) {
                    Log.d("QuizecViewModel", "Error saving quiz: $e")
                }
            }
        }
        _currentQuiz.value = null
    }

    fun deleteQuiz(quiz: Quiz) {
        viewModelScope.launch {
            try {
                SStorageUtil.deleteQuizDatabase(dbClient, quiz) { e ->
                    if (e != null) {
                        Log.d("QuizecViewModel", "Error deleting quiz 1: $e")
                    } else {
                        quizList.removeQuiz(quiz)
                    }
                }
            } catch (e: Throwable) {
                Log.d("QuizecViewModel", "Error deleting quiz 2: $e")
            }
        }
    }

    //TODO: separate event handling and data manipulation (viewmodel and util)
    fun createLobby(
        quizId: Long,
        duration: Long
        //TODO: add more parameters for the lobby (show on start/wait, location request, etc)
    ) {
        viewModelScope.launch {
            try {
                val resultLobby = dbClient.from("lobby").insert(
                    Lobby(CodeGen.genLobbyCode(), SAuthUtil.currentUser!!.id, quizId, duration)
                ) { select() }.decodeSingleOrNull<Lobby>()

                //TODO: check if lobby was created successfully in the database
                // - if is because of the code, gen another one and try again
                // - if server or unknown error, throw exception or something like that

                _currentLobby.value = resultLobby
            } catch (e: Exception) {
                Log.e("QuizecViewModel", "createLobby: ${e.message}")
            }
        }
    }

    //TODO: separate event handling and data manipulation (viewmodel and util)
    fun joinLobby(lobbyCode: String) {
        viewModelScope.launch {
            try {
                val lobby = dbClient.from("lobby").select {
                    filter {
                        eq("lobby_code", lobbyCode)
                    }
                }.decodeSingleOrNull<Lobby>() ?: throw Exception("Lobby not found")

                //TODO: check block
                // - if user is the owner, throw exception(?)
                // - if user is already in the lobby, throw exception(?)
                // - if doesn't exist, throw exception(?)
                // - if server or unknown error, throw exception or something like that

                Log.d("QuizecViewModel", "joinLobby: $lobby")

                dbClient.from("lobby_user").insert(
                    hashMapOf(
                        "lobby_code" to lobby.code,
                        "user_id" to SAuthUtil.currentUser!!.id
                    )
                )

                _currentLobby.value = lobby

                Log.d("QuizecViewModel", "currentLobby: $_currentLobby")
            } catch (e: Exception) {
                Log.e("QuizecViewModel", "joinLobby: ${e.message}")
            }
        }
    }
}