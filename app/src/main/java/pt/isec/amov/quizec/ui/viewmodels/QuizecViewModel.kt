package pt.isec.amov.quizec.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.launch
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionList
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.model.quiz.QuizList
import pt.isec.amov.quizec.utils.SStorageUtil

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
            quizList.updateQuiz(quiz)
        } else {
            quizList.addQuiz(quiz)
        }
        _currentQuiz.value = null
    }

    fun deleteQuiz(quiz: Quiz) {
        quizList.removeQuiz(quiz)
    }
}