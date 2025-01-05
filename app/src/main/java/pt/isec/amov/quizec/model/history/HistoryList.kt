package pt.isec.amov.quizec.model.history

import androidx.compose.runtime.mutableStateListOf

class HistoryList {
    private val _list = mutableStateListOf<History>()
    val list: MutableList<History> = _list

    fun setHistoryList(list: List<History>) {
        _list.clear()
        _list.addAll(list)
    }

    fun getHistoryList(): List<History> {
        return _list.sortedBy { it.quiz.title }
    }

    fun addHistory(history: History) {
        _list.add(history)
    }

    fun removeHistory(history: History) {
        _list.remove(history)
    }

    fun updateHistory(history: History) {
        val index = _list.indexOfFirst { it.id == history.id }
        if (index != -1) {
            _list[index] = history
        }
    }

    fun clear() {
        _list.clear()
    }
}