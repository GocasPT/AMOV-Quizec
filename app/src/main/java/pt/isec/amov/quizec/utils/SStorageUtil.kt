package pt.isec.amov.quizec.utils

import android.content.res.AssetManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import pt.isec.amov.quizec.QuizecApp
import pt.isec.amov.quizec.model.QuizQuestion
import pt.isec.amov.quizec.model.history.History
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.quiz.Quiz
import java.io.File
import java.io.InputStream

class SStorageUtil {
    //TODO: improve this util class
    // - use Result → Result.success/Result.failure → onSuccess/onFailure
    // - use require → validate inputs

    companion object {
        private val dbClient get() = QuizecApp.getInstance().dbClient

        suspend fun saveQuestionDatabase(
            question: Question,
            onResult: (Throwable?, Question?) -> Unit
        ) {
            try {
                val updatedQuestion = dbClient.from("question").insert(question) {
                    select()
                }.decodeSingle<Question>()
                onResult(null, updatedQuestion)
            } catch (e: Throwable) {
                onResult(e, null)
            }
        }

        suspend fun updateQuestionDatabase(question: Question, onResult: (Throwable?) -> Unit) {
            try {
                dbClient.from("question").update(question) {
                    filter {
                        eq("id", question.id!!)
                    }
                }
                onResult(null)
            } catch (e: Throwable) {
                onResult(e)
            }
        }

        suspend fun deleteQuestionDatabase(question: Question, onResult: (Throwable?) -> Unit) {
            try {
                dbClient.from("question").delete {
                    filter {
                        eq("id", question.id!!)
                    }
                }
                onResult(null)
            } catch (e: Throwable) {
                onResult(e)
            }
        }

        suspend fun saveQuizDatabase(quiz: Quiz, onResult: (Throwable?, Quiz?) -> Unit) {
            try {
                val updatedQuiz = dbClient.from("quiz")
                    .insert(quiz.copy(questions = null)) {
                        select()
                    }
                    .decodeSingle<Quiz>()

                quiz.questions?.forEach { question ->
                    dbClient.from("quiz_question")
                        .insert(QuizQuestion(updatedQuiz.id!!, question.id!!))
                }

                quiz.image?.let {
                    uploadFile(dbClient, "quizzes", quiz.image)
                }

                quiz.id = updatedQuiz.id
                onResult(null, quiz)
            } catch (e: Throwable) {
                onResult(e, null)
            }
        }

        suspend fun updateQuizDatabase(quiz: Quiz, onResult: (Throwable?) -> Unit) {
            try {
                dbClient.from("quiz").update(
                    {
                        set("title", quiz.title)
                    }
                ) {
                    filter {
                        eq("id", quiz.id!!)
                    }
                }

                dbClient.from("quiz_question").delete {
                    filter {
                        eq("quiz_id", quiz.id!!)
                    }
                }

                quiz.questions?.forEach { question ->
                    dbClient.from("quiz_question").insert(QuizQuestion(quiz.id!!, question.id!!))
                }

                onResult(null)
            } catch (e: Throwable) {
                onResult(e)
            }
        }

        suspend fun deleteQuizDatabase(
            dbClient: SupabaseClient,
            quiz: Quiz,
            onResult: (Throwable?) -> Unit
        ) {
            try {
                dbClient.from("quiz_question").delete {
                    filter {
                        eq("quiz_id", quiz.id!!)
                    }
                }

                dbClient.from("quiz").delete {
                    filter {
                        eq("id", quiz.id!!)
                    }
                }
                onResult(null)
            } catch (e: Throwable) {
                onResult(e)
            }
        }

        suspend fun saveHistoryDatabase(dbClient: SupabaseClient, history: History, onResult: (Throwable?) -> Unit) {
            try {
                dbClient.from("history").insert(history)
                onResult(null)
            } catch (e: Throwable) {
                onResult(e)
            }
        }

        //TODO: add listener
        //private var listenerRegistration: ListenerRegistration? = null

        /*
        fun startObserver(onNewValues: (Long, Long) -> Unit) {
            stopObserver()
            val db = Firebase.firestore
            listenerRegistration = db.collection("Scores").document("Level1")
                .addSnapshotListener { docSS, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    if (docSS != null && docSS.exists()) {
                        val nrGames = docSS.getLong("nrgames") ?: 0
                        val topScore = docSS.getLong("topscore") ?: 0
                        Log.i("Firestore", "$nrGames : $topScore")
                        onNewValues(nrGames, topScore)
                    }
                }
        }

        fun stopObserver() {
            listenerRegistration?.remove()
        }
        */

        //TODO: Flow VS .channel()

        fun getFileFromAsset(assetManager: AssetManager, strName: String): InputStream? {
            var inputStream: InputStream? = null
            try {
                inputStream = assetManager.open(strName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return inputStream
        }

        private suspend fun uploadFile(dbClient: SupabaseClient, bucket: String, file: String) {
            dbClient.storage.from(bucket).upload(
                file,
                File(file).readBytes()
            )
        }

        suspend fun loadFile(dbClient: SupabaseClient, bucket: String, file: String) {
            val localFile = File(file)

            if (localFile.exists()) {
                return
            }

            val bbucket = dbClient.storage.from(bucket)
            val bytes = bbucket.downloadAuthenticated(file)
            localFile.writeBytes(bytes)
        }
    }
}
