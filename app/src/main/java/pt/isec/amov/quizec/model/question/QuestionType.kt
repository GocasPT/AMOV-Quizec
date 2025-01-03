package pt.isec.amov.quizec.model.question

import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.utils.Strings

enum class QuestionType(val displayName: String) {
    TRUE_FALSE(Strings.get(R.string.true_or_false)),
    SINGLE_CHOICE(Strings.get(R.string.single_choice)),
    MULTIPLE_CHOICE(Strings.get(R.string.multiple_choice)),
    MATCHING(Strings.get(R.string.matching)),
    ORDERING(Strings.get(R.string.ordering)),
    DRAG(Strings.get(R.string.drag)),

    //ASSOCIATION("Association"),
    FILL_BLANK(Strings.get(R.string.fill_in_the_blank)),
}