package pt.isec.amov.quizec.utils

class CodeGen {
    companion object {
        fun genLobbyCode(): String {
            val chars = ('A'..'Z') + ('0'..'9')
            return buildString {
                repeat(6) {
                    append(chars.random())
                }
            }
        }
    }
}