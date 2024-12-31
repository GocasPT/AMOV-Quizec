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
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionList
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.model.quiz.QuizList
import pt.isec.amov.quizec.utils.SAuthUtil

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
}