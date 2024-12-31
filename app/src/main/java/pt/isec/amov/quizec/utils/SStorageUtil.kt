package pt.isec.amov.quizec.utils

import android.content.res.AssetManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import pt.isec.amov.quizec.model.question.Question
import java.io.InputStream

class SStorageUtil {
    companion object {
        val quiz = hashMapOf(
            "title" to "Quiz X",
        )

        suspend fun saveQuestionDatabase(dbClient: SupabaseClient, question: Question, onResult: (Throwable?, Question?) -> Unit) {
            try {
                val updatedQuestion = dbClient.from("question").insert(question.copy(id = null)) {
                    select()
                }.decodeSingle<Question>()
                onResult(null, updatedQuestion)
            } catch (e: Throwable) {
                onResult(e, null)
            }
        }

        suspend fun updateQuestionDatabase(dbClient: SupabaseClient, question: Question, onResult: (Throwable?) -> Unit) {
            try {
                val updatedQuestion = dbClient.from("question").update(question) {
                    filter {
                        eq("id", question.id!!)
                    }
                }
                onResult(null)
            } catch (e: Throwable) {
                onResult(e)
            }
        }

        suspend fun deleteQuestionDatabase(dbClient: SupabaseClient, question: Question, onResult: (Throwable?) -> Unit) {
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

        //TODO: improve onResult handler
        suspend fun addDataToSupabase(dbClient: SupabaseClient, onResult: (Throwable?) -> Unit) {
            dbClient.from("quiz").insert(quiz)
            onResult(null)
        }

        //TODO: improve onResult handler
        suspend fun updateDataInSupabase(dbClient: SupabaseClient, onResult: (Throwable?) -> Unit) {
            dbClient.from("quiz").upsert(quiz)
            onResult(null)
        }

        //TODO: improve onResult handler
        suspend fun updateDateInSupabaseTrans(
            dbClient: SupabaseClient,
            onResult: (Throwable?) -> Unit
        ) {
            dbClient.from("quiz").upsert(quiz)
            onResult(null)
        }

        //TODO: improve onResult handler
        suspend fun removeDataFromSupabase(
            dbClient: SupabaseClient,
            onResult: (Throwable?) -> Unit
        ) {
            dbClient.from("quiz").delete()
            onResult(null)
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