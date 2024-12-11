package pt.isec.amov.quizec.utils

class QuestionIDGenerator {
    companion object {
        private var currentId = 0

        fun getNextId(): Int {
            currentId += 1
            return currentId
        }
    }
}