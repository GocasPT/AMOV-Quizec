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

    fun removeQuiz(quiz: Quiz) {
        _list.remove(quiz)
    }
}