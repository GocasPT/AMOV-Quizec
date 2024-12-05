package pt.isec.amov.quizec

import android.app.Application
import pt.isec.amov.quizec.model.question.Answer.TrueFalse
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionIDGenerator
import pt.isec.amov.quizec.model.question.QuestionList
import pt.isec.amov.quizec.model.question.QuestionType
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.model.quiz.QuizList

class QuizecApp : Application() {
    //TODO: add local data for testing (no database for now)
    private val _questionList: QuestionList by lazy {
        QuestionList().apply {
            addQuestion(
                Question(
                    QuestionIDGenerator.getNextId(), "Are you insane?", QuestionType.YES_NO, null, listOf(
                        TrueFalse(true)
                    )
                )
            )
        }
    }

    private val _quizList: QuizList by lazy {
        QuizList().apply {
            addQuiz(
                Quiz(
                    "XXXXX", "Quiz 0", null, listOf(
                        _questionList.getQuestionList()[0]
                    ), false, 0, false, true
                )
            )
        }
    }

    val quizList: QuizList
        get() = _quizList

    val questionList: QuestionList
        get() = _questionList
}