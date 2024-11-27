package pt.isec.amov.quizec

import android.app.Application
import pt.isec.amov.quizec.model.Question
import pt.isec.amov.quizec.model.QuestionType
import pt.isec.amov.quizec.model.Quiz

class QuizecApp : Application() {
    //TODO: add local data for testing (no database for now)
    var _quizList: List<Quiz> = listOf(
        Quiz("Quiz 1", listOf(
            Question("Question 1", QuestionType.YES_NO),
            Question("Question 2", QuestionType.MULTIPLE_CHOICE),
        )),
        Quiz("Quiz 2", listOf(
            Question("Question 1", QuestionType.SINGLE_CHOICE),
            Question("Question 2", QuestionType.FILL_BLANK),
            Question("Question 3", QuestionType.ASSOCIATION),
        )),
        Quiz("Quiz 3", listOf())
    )
    val quizList: List<Quiz>
        get() = _quizList
}