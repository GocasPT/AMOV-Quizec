package pt.isec.amov.quizec.model.question

class QuestionList {
    private val _list = mutableListOf<Question>()

    fun getQuestionList(): List<Question> {
        //return _list // No order
        return _list.sortedBy { it.content } // Sorted by question
    }

    fun addQuestion(question: Question) {
        _list.add(question)
    }

    fun removeQuestion(question: Question) {
        _list.remove(question)
    }

    fun updateQuestion(question: Question) {
        val index = _list.indexOfFirst { it.id == question.id }
        if (index != -1) {
            _list[index] = question
        }
    }
}