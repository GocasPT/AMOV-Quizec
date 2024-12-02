package pt.isec.amov.quizec.ui.viewmodels

import androidx.lifecycle.ViewModel
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionList
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.model.quiz.QuizList

class QuizecViewModel(val questionList: QuestionList, val quizList: QuizList) : ViewModel() {
    //TODO: add data variables

    fun createQuestion() {

    }

    fun selectQuestion() {
        //TODO: select question
    }

    fun saveQuestion(question: Question) {
        questionList.addQuestion(question)
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