package pt.isec.amov.quizec.utils

import android.content.res.AssetManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.model.QuizQuestion
import pt.isec.amov.quizec.model.question.Question
import pt.isec.amov.quizec.model.quiz.Quiz
import java.io.InputStream

class SStorageUtil {
    companion object {
        suspend fun saveQuestionDatabase(dbClient: SupabaseClient, question: Question, onResult: (Throwable?, Question?) -> Unit) {
            try {
                val updatedQuestion = dbClient.from(Strings.get(R.string.DB_question)).insert(question) {
                    select()
                }.decodeSingle<Question>()
                onResult(null, updatedQuestion)
            } catch (e: Throwable) {
                onResult(e, null)
            }
        }

        suspend fun updateQuestionDatabase(dbClient: SupabaseClient, question: Question, onResult: (Throwable?) -> Unit) {
            try {
                dbClient.from(Strings.get(R.string.DB_question)).update(question) {
                    filter {
                        eq(Strings.get(R.string.DB_id), question.id!!)
                    }
                }
                onResult(null)
            } catch (e: Throwable) {
                onResult(e)
            }
        }

        suspend fun deleteQuestionDatabase(dbClient: SupabaseClient, question: Question, onResult: (Throwable?) -> Unit) {
            try {
                dbClient.from(Strings.get(R.string.DB_question)).delete {
                    filter {
                        eq(Strings.get(R.string.DB_id), question.id!!)
                    }
                }
                onResult(null)
            } catch (e: Throwable) {
                onResult(e)
            }
        }

        suspend fun saveQuizDatabase(dbClient: SupabaseClient, quiz: Quiz, onResult: (Throwable?, Quiz?) -> Unit) {
            try {
                val updatedQuiz = dbClient.from(Strings.get(R.string.DB_quiz))
                    .insert(quiz.copy(questions = null)) {
                        select()
                    }
                    .decodeSingle<Quiz>()

                quiz.questions?.forEach { question ->
                    dbClient.from(Strings.get(R.string.DB_quiz_question)).insert(QuizQuestion(updatedQuiz.id!!, question.id!!))
                }

                quiz.id = updatedQuiz.id
                onResult(null, quiz)
            } catch (e: Throwable) {
                onResult(e, null)
            }
        }

        suspend fun updateQuizDatabase(dbClient: SupabaseClient, quiz: Quiz, onResult: (Throwable?) -> Unit) {
            try {
                dbClient.from(Strings.get(R.string.DB_quiz)).update(
                    {
                        set(Strings.get(R.string.DB_title), quiz.title)
                    }
                )  {
                    filter {
                        eq(Strings.get(R.string.DB_id), quiz.id!!)
                    }
                }

                dbClient.from(Strings.get(R.string.DB_quiz_question)).delete {
                    filter {
                        eq(Strings.get(R.string.DB_quiz_id), quiz.id!!)
                    }
                }

                quiz.questions?.forEach { question ->
                    dbClient.from(Strings.get(R.string.DB_quiz_question)).insert(QuizQuestion(quiz.id!!, question.id!!))
                }

                onResult(null)
            } catch (e: Throwable) {
                onResult(e)
            }
        }

        suspend fun deleteQuizDatabase(dbClient: SupabaseClient, quiz: Quiz, onResult: (Throwable?) -> Unit) {
            try {
                dbClient.from(Strings.get(R.string.DB_quiz_question)).delete {
                    filter {
                        eq(Strings.get(R.string.DB_quiz_id), quiz.id!!)
                    }
                }

                dbClient.from(Strings.get(R.string.DB_quiz)).delete {
                    filter {
                        eq(Strings.get(R.string.DB_id), quiz.id!!)
                    }
                }
                onResult(null)
            } catch (e: Throwable) {
                onResult(e)
            }
        }

        //TODO: add listener
        //private var listenerRegistration: ListenerRegistration? = null

        /*
        fun startObserver(dbClient: SupabaseClient, onNewValues: (Long, Long) -> Unit) {
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

        fun uploadFile(inputStream: InputStream, imgFile: String) {

        }
    }
}