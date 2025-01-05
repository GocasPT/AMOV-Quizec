package pt.isec.amov.quizec.model.question

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.ui.graphics.vector.ImageVector
import pt.isec.amov.quizec.R
import pt.isec.amov.quizec.utils.Strings

enum class QuestionType(val displayName: String, val icon: ImageVector) {
    TRUE_FALSE(Strings.get(R.string.true_or_false), Icons.Filled.ToggleOn),
    SINGLE_CHOICE(Strings.get(R.string.single_choice), Icons.Filled.CheckCircle),
    MULTIPLE_CHOICE(Strings.get(R.string.multiple_choice), Icons.Filled.Checklist),
    MATCHING(Strings.get(R.string.matching), Icons.Filled.ContentCopy),
    ORDERING(Strings.get(R.string.ordering), Icons.Filled.FormatListNumbered),
    DRAG(Strings.get(R.string.drag), Icons.Filled.DragIndicator),
    FILL_BLANK(Strings.get(R.string.fill_blank), Icons.Filled.Edit),
}