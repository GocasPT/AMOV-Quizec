package pt.isec.amov.quizec.utils

import android.content.res.AssetManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import java.io.InputStream

class SStorageUtil {
    companion object {
        //TODO: how to share the dbClient between classes?
        private val _dbClient: SupabaseClient by lazy {
            createSupabaseClient(
                supabaseUrl = "https://vivtabksraqgsyzulwgz.supabase.co",
                supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZpdnRhYmtzcmFxZ3N5enVsd2d6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzM4NzIyMTEsImV4cCI6MjA0OTQ0ODIxMX0.ZH3W7Ar1L7HGAmifvFdkJ7UuQiVRrNk8ndS5viBfKrc"
            ) {
                install(Auth)
                install(Storage)
                install(Realtime)
                install(Postgrest)
            }
        }

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
        //TODO: Bucket VS Folder VS File

        fun getFileFromAsset(assetManager: AssetManager, strName: String): InputStream? {
            var inputStream: InputStream? = null
            try {
                inputStream = assetManager.open(strName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return inputStream
        }

        fun fetchImageUrl(bucketName: String, path: String): String? {
            return try {
                _dbClient.storage.from(bucketName).publicUrl(path)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        //TODO: return ByteArray OR save in local cache + InputStream?
        suspend fun downloadFile(inputStream: InputStream, imgFile: String): ByteArray {
            val bucket = _dbClient.storage.from("images")
            val bytes = bucket.downloadAuthenticated(imgFile)
            return bytes
        }

        suspend fun uploadFile(inputStream: InputStream, imgFile: String) {
            val bucket = _dbClient.storage.from("images")
            bucket.upload(imgFile, inputStream.readBytes()) {
                upsert = false
            }
        }
    }
}