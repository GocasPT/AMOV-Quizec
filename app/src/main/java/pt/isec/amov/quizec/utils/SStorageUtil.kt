package pt.isec.amov.quizec.utils

import android.content.res.AssetManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import java.io.InputStream

class SStorageUtil {
    companion object {
        val quiz = hashMapOf(
            "title" to "Quiz X",
        )

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