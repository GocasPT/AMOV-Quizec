package pt.isec.amov.quizec.model.quiz

class QuizIDGenerator {
    companion object {
        private val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        private const val length = 5

        fun generateRandomCode(): String {
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }
}