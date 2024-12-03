package pt.isec.amov.quizec.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionList
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.model.quiz.QuizList

class QuizecViewModel(val questionList: QuestionList, val quizList: QuizList) : ViewModel() {
    //TODO: add data variables
    private var _currentQuiz = mutableStateOf<Quiz?>(null)
    val currentQuiz: Quiz? get() = _currentQuiz.value

    fun createQuestion() {

    }

    fun selectQuestion() {
        //TODO: select question
    }

    fun saveQuestion(question: Question) {
        questionList.addQuestion(question)
    }

    fun createQuiz() {
        _currentQuiz.value  = null
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
}