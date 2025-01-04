package pt.isec.amov.quizec.model.quiz

import androidx.compose.runtime.mutableStateListOf

class QuizList {
    private val _list = mutableStateListOf<Quiz>()

    fun getQuizList(): List<Quiz> {
        //return _list // No order
        return _list.sortedBy { it.title } // Sorted by title
    }

    fun addQuiz(quiz: Quiz) {
        _list.add(quiz)
    }

    fun updateQuiz(quiz: Quiz) {
        val index = _list.indexOfFirst { it.id == quiz.id }
        if (index != -1) {
            _list[index] = quiz
        }
    }

    fun removeQuiz(quiz: Quiz) {
        _list.remove(quiz)
    }

    fun clear() {
        _list.clear()
    }
}