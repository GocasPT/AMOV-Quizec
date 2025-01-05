package pt.isec.amov.quizec.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Lobby(
    @SerialName("lobby_code")
    val code: String,
    @SerialName("owner_user_id")
    val ownerUUID: String,
    @SerialName("quiz_id")
    val quizId: Long,
    val started: Boolean = false,
    val duration: Long,
    @SerialName("expired_at")
    val expiredAt: Instant?,
)
