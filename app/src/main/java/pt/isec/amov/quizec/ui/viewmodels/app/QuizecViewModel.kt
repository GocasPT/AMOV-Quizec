package pt.isec.amov.quizec.ui.viewmodels.app

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import pt.isec.amov.quizec.model.Lobby
import pt.isec.amov.quizec.model.User
import pt.isec.amov.quizec.model.history.AnswerHistory
import pt.isec.amov.quizec.model.history.History
import pt.isec.amov.quizec.model.history.HistoryList
import pt.isec.amov.quizec.model.history.QuizSnapshot
import pt.isec.amov.quizec.model.question.Answer.FillBlank
import pt.isec.amov.quizec.model.question.Answer.Matching
import pt.isec.amov.quizec.model.question.Answer.MultipleChoice
import pt.isec.amov.quizec.model.question.Answer.Ordering
import pt.isec.amov.quizec.model.question.Answer.SingleChoice
import pt.isec.amov.quizec.model.question.Answer.TrueFalse
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.question.QuestionList
import pt.isec.amov.quizec.model.quiz.Quiz
import pt.isec.amov.quizec.model.quiz.QuizList
import pt.isec.amov.quizec.utils.CodeGen
import pt.isec.amov.quizec.utils.Constants
import pt.isec.amov.quizec.utils.Constants.HISTORY_TABLE
import pt.isec.amov.quizec.utils.Constants.QUESTION_TABLE
import pt.isec.amov.quizec.utils.Constants.QUIZ_TABLE
import pt.isec.amov.quizec.utils.SAuthUtil
import pt.isec.amov.quizec.utils.SRealTimeUtil
import pt.isec.amov.quizec.utils.SStorageUtil

class QuizecViewModel(private val dbClient: SupabaseClient) : ViewModel() {
    private val _questionList = QuestionList()
    private val _quizList = QuizList()
    private val _historyList = HistoryList()
    private var _currentQuiz = mutableStateOf<Quiz?>(null)
    private var _currentQuestion = mutableStateOf<Question?>(null)
    private var _currentHistory = mutableStateOf<History?>(null)
    private var _currentLobbiesList = mutableListOf<Lobby>()
    private var _currentLobby = mutableStateOf<Lobby?>(null)
    private var _currentLobbyStarted = mutableStateOf(false)
    private var _currentLobbyPlayerCount = mutableIntStateOf(0)
    private var _currentLobbyPlayers = mutableListOf<User>()

    val questionList: MutableList<Question> get() = _questionList.list
    val quizList: MutableList<Quiz> get() = _quizList.list
    val historyList: MutableList<History> get() = _historyList.list
    val currentQuiz: Quiz? get() = _currentQuiz.value
    val currentQuestion: Question? get() = _currentQuestion.value
    val currentHistory: History? get() = _currentHistory.value
    val currentLobbiesList: MutableList<Lobby> get() = _currentLobbiesList
    val currentLobby: State<Lobby?> get() = _currentLobby
    val currentLobbyStarted: State<Boolean> get() = _currentLobbyStarted
    val currentLobbyPlayerCount: State<Int> get() = _currentLobbyPlayerCount
    val currentLobbyPlayers: MutableList<User> get() = _currentLobbyPlayers

    suspend fun fetchData() {
        fetchQuiz()
        fetchQuestion()
        fetchHistory()
    }

    suspend fun fetchQuiz() {
        Log.d("QuizecViewModel", "fetchQuiz: ${SAuthUtil.currentUser!!.id}")

        dbClient.from(QUIZ_TABLE).select {
            filter { eq("owner", SAuthUtil.currentUser!!.id) }
        }.decodeList<Quiz>().let { quizList ->
            quizList.forEach { quiz ->
                val questions = dbClient
                    .from(QUESTION_TABLE)
                    .select(Columns.raw("*, quiz_question!inner(*)")) {
                        filter {
                            eq("quiz_question.quiz_id", quiz.id!!)
                        }
                    }
                    .decodeList<Question>()

                quiz.questions = questions
                quiz.image?.let {
                    getQuizImage(it)
                }
                _quizList.addQuiz(quiz)
            }
        }

        Log.d("QuizecViewModel", "_quizList: ${_quizList.getQuizList()}")
    }

    suspend fun fetchQuestion() {
        Log.d("QuizecViewModel", "fetchQuestion: ${SAuthUtil.currentUser!!.id}")

        dbClient.from(QUESTION_TABLE).select {
            filter { eq("user_id", SAuthUtil.currentUser!!.id) }
        }.decodeList<Question>().let { list ->
            list.forEach {
                it.image?.let { image ->
                    getQuestionImage(image)
                }
                _questionList.addQuestion(it)
            }
        }

        Log.d("QuizecViewModel", "_questionList: ${_questionList.getQuestionList()}")
    }

    suspend fun fetchHistory() {
        Log.d("QuizecViewModel", "fetchHistory: ${SAuthUtil.currentUser!!.id}")

        dbClient.from(HISTORY_TABLE).select {
            filter { eq("user_id", SAuthUtil.currentUser!!.id) }
        }.decodeList<History>().let { list ->
            list.forEach {
                it.quiz.image?.let { image ->
                    getQuizImage(image)
                }
                _historyList.addHistory(it)
            }
        }

        Log.d("QuizecViewModel", "_historyList: ${_historyList.getHistoryList()}")
    }

    fun clearData() {
        _currentQuiz.value = null
        _currentQuestion.value = null
        _currentHistory.value = null
        questionList.clear()
        quizList.clear()
        historyList.clear()
    }

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
                    SStorageUtil.updateQuestionDatabase(question) { e ->
                        if (e != null) {
                            Log.d("QuizecViewModel", "Error updating question: $e")
                        } else {
                            _questionList.updateQuestion(question)
                        }
                    }
                } catch (e: Throwable) {
                    Log.d("QuizecViewModel", "Error updating question: $e")
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    SStorageUtil.saveQuestionDatabase(question) { e, updatedQuestion ->
                        if (e != null) {
                            Log.d("QuizecViewModel", "Error saving question: $e")
                        } else {
                            _questionList.addQuestion(updatedQuestion!!)
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
                SStorageUtil.deleteQuestionDatabase(question) { e ->
                    if (e != null) {
                        Log.d("QuizecViewModel", "Error deleting question: $e")
                    } else {
                        _questionList.removeQuestion(question)
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
                    SStorageUtil.updateQuizDatabase(quiz) { e ->
                        if (e != null) {
                            Log.d("QuizecViewModel", "Error updating quiz: $e")
                        } else {
                            _quizList.updateQuiz(quiz)
                        }
                    }
                } catch (e: Throwable) {
                    Log.d("QuizecViewModel", "Error updating quiz: $e")
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    SStorageUtil.saveQuizDatabase(quiz) { e, updatedQuiz ->
                        if (e != null) {
                            Log.d("QuizecViewModel", "Error saving quiz: $e")
                        } else {
                            _quizList.addQuiz(updatedQuiz!!)
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
                SStorageUtil.deleteQuizDatabase(quiz) { e ->
                    if (e != null) {
                        Log.d("QuizecViewModel", "Error deleting quiz 1: $e")
                    } else {
                        _quizList.removeQuiz(quiz)
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

    //TODO: delete this later
    fun createDummyHistory(userId: String?) {
        val timezone = TimeZone.currentSystemDefault()
        //Owner = creator1@gmail.com Pass = 123123
        val addHistory = History(
            id = null, //database dynamically assigns id
            userId = userId!!,
            quiz = QuizSnapshot(
                title = "Dummy Quiz History",
                image = "/data/user/0/pt.isec.amov.quizec/files/image903666447683273588.jpg",
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
                SStorageUtil.saveHistoryDatabase(addHistory) { e ->
                    if (e != null) {
                        Log.d("QuizecViewModel", "Error saving history: $e")
                    } else {
                        _historyList.addHistory(addHistory)
                    }
                }
            } catch (e: Throwable) {
                Log.d("QuizecViewModel", "Error saving history: $e")
            }
        }
    }

    fun getQuizImage(imageName: String) {
        viewModelScope.launch {
            try {
                SStorageUtil.loadFile("quizzes", imageName)
            } catch (e: Throwable) {
                Log.d("QuizecViewModel", "Error getting quiz image: $e")
            }
        }
    }

    fun getQuestionImage(imageName: String) {
        viewModelScope.launch {
            try {
                SStorageUtil.loadFile("questions", imageName)
            } catch (e: Throwable) {
                Log.d("QuizecViewModel", "Error getting question image: $e")
            }
        }
    }

    fun getLobbies() {
        viewModelScope.launch {
            try {
                Log.d("QuizecViewModel", "getLobbies: ${SAuthUtil.currentUser!!.id}")

                val resultLobbies = dbClient.from("lobby").select {
                    filter {
                        eq("owner_user_id", SAuthUtil.currentUser!!.id)
                    }
                }.decodeList<Lobby>()

                Log.d("QuizecViewModel", "getLobbies: $resultLobbies")

                _currentLobbiesList.clear()
                _currentLobbiesList.addAll(resultLobbies)
            } catch (e: Exception) {
                Log.e("QuizecViewModel", "getLobbies: ${e.message}")
            }
        }
    }

    //TODO: separate event handling and data manipulation (viewmodel and util)
    fun createLobby(
        quizId: Long,
        started: Boolean,
        localRestricted: Boolean,
        duration: Long
        //TODO: add more parameters for the lobby (show on start/wait, location request, etc)
    ) {
        viewModelScope.launch {
            try {
                Log.d(
                    "QuizecViewModel",
                    "createLobby: $quizId, $started, $localRestricted, $duration"
                )

                val resultLobby = dbClient.from("lobby").insert(
                    Lobby(
                        CodeGen.genLobbyCode(),
                        SAuthUtil.currentUser!!.id,
                        quizId,
                        started,
                        localRestricted,
                        duration,
                        null
                    )
                ) { select() }.decodeSingleOrNull<Lobby>()

                Log.d("QuizecViewModel", "createLobby: $resultLobby")

                //TODO: check if lobby was created successfully in the database
                // - if is because of the code, gen another one and try again
                // - if server or unknown error, throw exception or something like that

                _currentLobby.value = resultLobby
            } catch (e: Exception) {
                Log.e("QuizecViewModel", "createLobby: ${e.message}")
            }
        }
    }

    //TODO: separate event handling and data manipulation (viewmodel and util)
    fun joinLobby(lobbyCode: String) {
        viewModelScope.launch {
            try {
                val lobby = dbClient.from(Constants.LOBBY_TABLE).select {
                    filter {
                        eq("lobby_code", lobbyCode)
                    }
                }.decodeSingleOrNull<Lobby>() ?: throw Exception("Lobby not found")

                //TODO: check block
                // - if user is the owner, throw exception(?)
                // - if user is already in the lobby, throw exception(?)
                // - if doesn't exist, throw exception(?)
                // - if server or unknown error, throw exception or something like that

                Log.d("QuizecViewModel", "joinLobby: $lobby")

                dbClient.from(Constants.LOBBY_USERS_TABLE).insert(
                    hashMapOf(
                        "lobby_code" to lobby.code,
                        "user_id" to SAuthUtil.currentUser!!.id
                    )
                )

                _currentLobby.value = lobby

                SRealTimeUtil.observerIfLobbyHaveStarted(lobby.code).collectLatest {
                    Log.d("QuizecViewModel", "[_currentLobbyStarted] joinLobby: $it")
                    _currentLobbyStarted.value = it
                }

                Log.d("QuizecViewModel", "currentLobby: $_currentLobby")
            } catch (e: Exception) {
                Log.e("QuizecViewModel", "joinLobby: ${e.message}")
            }
        }
    }

    fun getPlayerCount() {
        viewModelScope.launch {
            val flow: Flow<List<User>> = SRealTimeUtil.getFlowPlayer(_currentLobby.value!!.code)
            flow.collect {
                _currentLobbyPlayerCount.intValue = it.size

                //TODO: clear + add OR update/swap?
                _currentLobbyPlayers.clear()
                for (user in it)
                    _currentLobbyPlayers.add(user)
            }
        }
    }
}