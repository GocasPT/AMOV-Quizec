package pt.isec.amov.quizec.utils

import androidx.annotation.StringRes
import pt.isec.amov.quizec.QuizecApp

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return QuizecApp.getInstance().getString(stringRes, *formatArgs)
    }
}