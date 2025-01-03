package pt.isec.amov.quizec.utils

import android.util.Log
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import pt.isec.amov.quizec.QuizecApp
import pt.isec.amov.quizec.model.User

@Serializable
data class LobbyUser(
    val id: Int,
    val lobby_code: String,
    val user_id: String
)

class SRealTimeUtil {
    companion object {
        private val dbClient get() = QuizecApp.getInstance().dbClient

        @OptIn(SupabaseExperimental::class)
        fun getFlowPlayer(lobbyCode: String): Flow<List<User>> {
            val dataFlow = dbClient.from("lobby_user").selectAsFlow(
                LobbyUser::id,
                filter = FilterOperation("lobby_code", FilterOperator.EQ, lobbyCode)
            )

            Log.d("SRealTimeUtil", "dataFlow: $dataFlow")

            return dataFlow.map { lobbyUsers ->
                lobbyUsers.map { lobbyUser ->
                    Log.d("SRealTimeUtil", "lobbyUser: $lobbyUser")
                    Log.d("SRealTimeUtil", "userUUID: ${lobbyUser.user_id}")

                    val user = dbClient.from(Constants.PROFILES_TABLE).select {
                        filter { eq("id", lobbyUser.user_id) }
                    }.decodeSingle<User>()

                    Log.d("SRealTimeUtil", "user: $user")

                    //TODO: set user email
                    //user.email =

                    user
                }
            }
        }
    }
}