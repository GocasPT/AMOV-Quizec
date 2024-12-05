package pt.isec.amov.quizec.utils

class QuizIDGenerator {
    companion object {
        private val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')

        fun generateRandomCode(length: Int = 5): String {
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }
}