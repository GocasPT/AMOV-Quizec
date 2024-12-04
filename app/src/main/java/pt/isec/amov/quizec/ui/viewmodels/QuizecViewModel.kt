package pt.isec.amov.quizec.ui.viewmodels

import androidx.lifecycle.ViewModel
import pt.isec.amov.quizec.model.Question
import pt.isec.amov.quizec.model.QuestionList
import pt.isec.amov.quizec.model.Quiz
import pt.isec.amov.quizec.model.QuizList

class QuizecViewModel(val questionList: QuestionList, val quizList: QuizList) : ViewModel() {
    //TODO: add data variables
    var currentQuestion: Question? = null
    var currentQuiz: Quiz? = null

    fun createQuestion() {
        //TODO: create question
    }

    fun selectQuestion(question: Question) {
        currentQuestion = question
    }

    fun saveQuestion() {
        //TODO: save question
    }

    fun createQuiz() {
        //TODO: create quiz
    }

    fun selectQuiz(quiz: Quiz) {
        currentQuiz = quiz
    }

    fun saveQuiz() {
        //TODO: save quiz
    }
}