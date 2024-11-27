package pt.isec.amov.quizec.model

class QuestionList {
    private val _list = mutableListOf<Question>()

    fun getQuestionList(): List<Question> {
        //return _list // No order
        return _list.sortedBy { it.title } // Sorted by title
    }

    fun addQuestion(question: Question) {
        _list.add(question)
    }

    fun removeQuestion(question: Question) {
        _list.remove(question)
    }
}