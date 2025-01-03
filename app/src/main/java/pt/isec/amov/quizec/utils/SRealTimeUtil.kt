package pt.isec.amov.quizec.utils

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

            return dataFlow.map { lobbyUsers ->
                lobbyUsers.map { lobbyUser ->
                    val user = dbClient.from("profiles").select {
                        filter { eq("id", lobbyUser.user_id) }
                    }.decodeSingleOrNull<User>()
                    user ?: User()
                }
            }
        }
    }
}