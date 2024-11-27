package pt.isec.amov.quizec

import android.app.Application
import pt.isec.amov.quizec.model.Question
import pt.isec.amov.quizec.model.QuestionType
import pt.isec.amov.quizec.model.Quiz
import pt.isec.amov.quizec.model.QuizList

class QuizecApp : Application() {
    //TODO: add local data for testing (no database for now)
    private val _quizList: QuizList by lazy {
        QuizList().apply {
            addQuiz(Quiz("Quiz 1", listOf(
                Question("Question 1", QuestionType.YES_NO),
                Question("Question 2", QuestionType.MULTIPLE_CHOICE),
            )))
            addQuiz(Quiz("Quiz 2", listOf(
                Question("Question 1", QuestionType.SINGLE_CHOICE),
                Question("Question 2", QuestionType.FILL_BLANK),
                Question("Question 3", QuestionType.ASSOCIATION),
            )))
            addQuiz(Quiz("Quiz 3", listOf()))
        }
    }
    val quizList: QuizList
        get() = _quizList
}