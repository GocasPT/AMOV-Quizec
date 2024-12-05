package pt.isec.amov.quizec.model.question

enum class QuestionType(val string: String) {
    YES_NO("Yes or No"),                    // P01
    SINGLE_CHOICE("Single choice"),         // P02
    MULTIPLE_CHOICE("Multiple choice"),     // P03
    MATCHING("Matching"),                   // P04
    ORDERING("Ordering"),                   // P05
    DRAG("Dragging"),                       // P06
    //ASSOCIATION("Association"),           // P07
    FILL_BLANK("Fill the missing words")    // P08
}