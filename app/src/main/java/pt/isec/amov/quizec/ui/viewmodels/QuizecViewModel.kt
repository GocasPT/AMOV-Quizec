package pt.isec.amov.quizec.ui.viewmodels

import androidx.lifecycle.ViewModel
import pt.isec.amov.quizec.model.QuestionList
import pt.isec.amov.quizec.model.Quiz
import pt.isec.amov.quizec.model.QuizList

class QuizecViewModel(val questionList: QuestionList, val quizList: QuizList) : ViewModel() {
    //TODO: add data variables

    fun createQuestion() {

    }

    fun selectQuestion() {
        //TODO: select question
    }

    fun saveQuestion() {
        //TODO: save question
    }

    fun createQuiz() {

    }

    fun selectQuiz() {
        //TODO: select quiz
    }

    fun saveQuiz(quiz: Quiz) {
        quizList.addQuiz(quiz)
    }
}