package pt.isec.amov.quizec.model.question

class QuestionIDGenerator {
    companion object {
        private var currentId = 0

        fun getNextId(): Int {
            currentId += 1
            return currentId
        }
    }
}