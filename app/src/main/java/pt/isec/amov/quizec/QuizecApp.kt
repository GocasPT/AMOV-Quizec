package pt.isec.amov.quizec

import android.app.Application
import pt.isec.amov.quizec.model.Question
import pt.isec.amov.quizec.model.QuestionList
import pt.isec.amov.quizec.model.QuestionOption
import pt.isec.amov.quizec.model.QuestionType
import pt.isec.amov.quizec.model.Quiz
import pt.isec.amov.quizec.model.QuizList

class QuizecApp : Application() {
    //TODO: add local data for testing (no database for now)
    private val _questionList: QuestionList by lazy {
        QuestionList().apply {
            addQuestion(
                Question(
                    "Question 1", QuestionType.YES_NO, null, listOf(
                        QuestionOption("Batata"),
                        QuestionOption("Cenoura"),
                    )
                )
            )
            addQuestion(
                Question(
                    "Question 2", QuestionType.MULTIPLE_CHOICE, null, listOf(
                        QuestionOption("Batata"),
                        QuestionOption("Cenoura"),
                        QuestionOption("Ceboulra"),
                        QuestionOption("Alface"),
                    )
                )
            )
            addQuestion(
                Question(
                    "Question 3", QuestionType.SINGLE_CHOICE, "batata.png", listOf(
                        QuestionOption("Batata"),
                        QuestionOption("Cenoura"),
                        QuestionOption("Ceboulra"),
                        QuestionOption("Alface"),
                    )
                )
            )
            addQuestion(Question("Question 4", QuestionType.FILL_BLANK, null, listOf()))
            addQuestion(
                Question(
                    "Question 5", QuestionType.ASSOCIATION, null, listOf(
                        QuestionOption("Batata"),
                        QuestionOption("Cenoura"),
                        QuestionOption("Ceboulra"),
                        QuestionOption("Alface"),
                    )
                )
            )
        }
    }

    private val _quizList: QuizList by lazy {
        QuizList().apply {
            addQuiz(
                Quiz(
                    "Quiz 0", null, listOf(
                        _questionList.getQuestionList()[0],
                        _questionList.getQuestionList()[1],
                        _questionList.getQuestionList()[2],
                        _questionList.getQuestionList()[3],
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