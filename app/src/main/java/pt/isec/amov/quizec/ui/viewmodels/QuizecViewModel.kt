package pt.isec.amov.quizec.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import pt.isec.amov.quizec.model.history.AnswerHistory
import pt.isec.amov.quizec.model.history.History
import pt.isec.amov.quizec.model.history.QuizSnapshot
import pt.isec.amov.quizec.model.question.Answer.*
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionList
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.model.quiz.QuizList
import pt.isec.amov.quizec.utils.SStorageUtil

class QuizecViewModel(val dbClient: SupabaseClient) : ViewModel() {
    //TODO: PLACE_HOLDER
    val questionList: QuestionList = QuestionList()
    val quizList: QuizList = QuizList()
    private val _historyList = mutableStateOf<List<History>>(emptyList())
    val historyList get() = _historyList.value

    //TODO: add data variables
    private var _currentQuiz = mutableStateOf<Quiz?>(null)
    private var _currentQuestion = mutableStateOf<Question?>(null)
    private var _currentHistory = mutableStateOf<History?>(null)
    val currentQuiz: Quiz? get() = _currentQuiz.value
    val currentQuestion: Question? get() = _currentQuestion.value
    val currentHistory: History? get() = _currentHistory.value

    fun selectHistory(history: History) {
        _currentHistory.value = history
    }

    fun createQuestion() {
        _currentQuestion.value = null
    }

    fun selectQuestion(question: Question) {
        _currentQuestion.value = question
    }

    fun saveQuestion(question: Question) {
        if (_currentQuestion.value != null) {
            viewModelScope.launch {
                try {
                    SStorageUtil.updateQuestionDatabase(dbClient, question) { e ->
                        if (e != null) {
                            Log.d("QuizecViewModel", "Error updating question: $e")
                        } else {
                            questionList.updateQuestion(question)
                        }
                    }
                } catch (e: Throwable) {
                    Log.d("QuizecViewModel", "Error updating question: $e")
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    SStorageUtil.saveQuestionDatabase(dbClient, question) { e, updatedQuestion ->
                        if (e != null) {
                            Log.d("QuizecViewModel", "Error saving question: $e")
                        } else {
                            questionList.addQuestion(updatedQuestion!!)
                        }
                    }
                } catch (e: Throwable) {
                    Log.d("QuizecViewModel", "Error saving question: $e")
                }
            }
        }
        _currentQuestion.value = null
    }

    fun deleteQuestion(question: Question) {
        viewModelScope.launch {
            try {
                SStorageUtil.deleteQuestionDatabase(dbClient, question) { e ->
                    if (e != null) {
                        Log.d("QuizecViewModel", "Error deleting question: $e")
                    } else {
                        questionList.removeQuestion(question)
                    }
                }
            } catch (e: Throwable) {
                Log.d("QuizecViewModel", "Error deleting question: $e")
            }
        }
    }

    fun duplicateQuestion(question: Question) {
        saveQuestion(question.copy(id = null))
    }

    fun createQuiz() {
        _currentQuiz.value = null
    }

    fun selectQuiz(quiz: Quiz) {
        _currentQuiz.value = quiz
    }

    fun saveQuiz(quiz: Quiz) {
        if (_currentQuiz.value != null) {
            viewModelScope.launch {
                try {
                    SStorageUtil.updateQuizDatabase(dbClient, quiz) { e ->
                        if (e != null) {
                            Log.d("QuizecViewModel", "Error updating quiz: $e")
                        } else {
                            quizList.updateQuiz(quiz)
                        }
                    }
                } catch (e: Throwable) {
                    Log.d("QuizecViewModel", "Error updating quiz: $e")
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    SStorageUtil.saveQuizDatabase(dbClient, quiz) { e, updatedQuiz ->
                        if (e != null) {
                            Log.d("QuizecViewModel", "Error saving quiz: $e")
                        } else {
                            quizList.addQuiz(updatedQuiz!!)
                        }
                    }
                } catch (e: Throwable) {
                    Log.d("QuizecViewModel", "Error saving quiz: $e")
                }
            }
        }
        _currentQuiz.value = null
    }

    fun deleteQuiz(quiz: Quiz) {
        viewModelScope.launch {
            try {
                SStorageUtil.deleteQuizDatabase(dbClient, quiz) { e ->
                    if (e != null) {
                        Log.d("QuizecViewModel", "Error deleting quiz 1: $e")
                    } else {
                        quizList.removeQuiz(quiz)
                    }
                }
            } catch (e: Throwable) {
                Log.d("QuizecViewModel", "Error deleting quiz 2: $e")
            }
        }
    }

    fun duplicateQuiz(quiz: Quiz) {
        saveQuiz(quiz.copy(id = null))
    }

    fun createDummyHistory(userId: String?) {
        val timezone = TimeZone.currentSystemDefault()
        //Owner = creator1@gmail.com Pass = 123123
        val addHistory = History(
            id = null, //database dynamically assigns id
            userId = userId!!,
            quiz = QuizSnapshot(
                title = "Dummy Quiz History",
                image = null,
                owner = "ddcd359b-c73a-4ff1-8ee2-3c361a23ea91"
            ),
            answers = listOf(
                AnswerHistory(
                    content = "Is Kotlin a statically-typed programming language?",
                    image = null,
                    correctAnswer = TrueFalse(rightAnswer = true),
                    userAnswer = TrueFalse(rightAnswer = true),
                    score = 1.0
                ),
                AnswerHistory(
                    content = "Which of the following is a JVM language?",
                    image = null,
                    correctAnswer = SingleChoice(
                        answers = setOf(
                            true to "Kotlin",
                            false to "Python",
                            false to "JavaScript"
                        )
                    ),
                    userAnswer = SingleChoice(
                        answers = setOf(
                            true to "JavaScript"
                        )
                    ),
                    score = 0.0
                ),
                AnswerHistory(
                    content = "Select all functional programming languages:",
                    image = null,
                    correctAnswer = MultipleChoice(
                        answers = setOf(
                            true to "Haskell",
                            true to "Scala",
                            false to "C++"
                        )
                    ),
                    userAnswer = MultipleChoice(
                        answers = setOf(
                            true to "Haskell",
                            false to "Scala",
                            true to "C++"
                        )
                    ),
                    score = 0.0
                ),
                AnswerHistory(
                    content = "Match the programming languages to their creators:",
                    image = null,
                    correctAnswer = Matching(
                        pairs = setOf(
                            "Java" to "James Gosling",
                            "Python" to "Guido van Rossum",
                            "C++" to "Bjarne Stroustrup"
                        )
                    ),
                    userAnswer = Matching(
                        pairs = setOf(
                            "Java" to "James Gosling",
                            "Python" to "Bjarne Stroustrup",
                            "C++" to "Guido van Rossum"
                        )
                    ),
                    score = 0.0
                ),
                AnswerHistory(
                    content = "Order the numbers from smallest to largest:",
                    image = null,
                    correctAnswer = Ordering(
                        order = listOf("1", "2", "3", "4", "5")
                    ),
                    userAnswer = Ordering(
                        order = listOf("1", "3", "2", "4", "5")
                    ),
                    score = 0.6
                ),
                AnswerHistory(
                    content = "Fill in the blanks: Kotlin is ___ and ___.",
                    image = null,
                    correctAnswer = FillBlank(
                        answers = setOf(
                            1 to "statically-typed",
                            2 to "cross-platform"
                        )
                    ),
                    userAnswer = FillBlank(
                        answers = setOf(
                            1 to "statically-typed",
                            2 to "open-source"
                        )
                    ),
                    score = 0.5
                )
            ),
            score = 7, //total score = sum of all questions (answer score * (20 / number of questions))
            date = Clock.System.now().toLocalDateTime(timezone).toString()
        )

        viewModelScope.launch {
            try {
                SStorageUtil.saveHistoryDatabase(dbClient, addHistory) { e ->
                    if (e != null) {
                        Log.d("QuizecViewModel", "Error saving history: $e")
                    }
                }
            } catch (e: Throwable) {
                Log.d("QuizecViewModel", "Error saving history: $e")
            }
        }
    }

    fun getHistory(userId: String?) {
        viewModelScope.launch {
            try {
                SStorageUtil.getHistoryDatabase(dbClient, userId) { e, historyList ->
                    if (e != null) {
                        Log.d("QuizecViewModel", "Error getting history: $e")
                    } else {
                        _historyList.value = historyList!!
                    }
                }
            } catch (e: Throwable) {
                Log.d("QuizecViewModel", "Error getting history: $e")
            }
        }
    }
}