package pt.isec.amov.quizec.model.quiz

class QuizList {
    private val _list = mutableListOf<Quiz>()

    fun getQuizList(): List<Quiz> {
        //return _list // No order
        return _list.sortedBy { it.title } // Sorted by title
    }

    fun addQuiz(quiz: Quiz) {
        _list.add(quiz)
    }

    fun getQuiz(id: String): Quiz? {
        return _list.find { it.id == id }
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
}