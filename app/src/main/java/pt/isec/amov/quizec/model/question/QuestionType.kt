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

enum class QuestionType(val displayName: String, val icon: ImageVector) {
    TRUE_FALSE("True or False", Icons.Filled.ToggleOn),
    SINGLE_CHOICE("Single Choice", Icons.Filled.CheckCircle),
    MULTIPLE_CHOICE("Multiple Choice", Icons.Filled.Checklist),
    MATCHING("Matching", Icons.Filled.ContentCopy),
    ORDERING("Ordering", Icons.Filled.FormatListNumbered),
    DRAG("Drag", Icons.Filled.DragIndicator),

    //ASSOCIATION("Association"),
    FILL_BLANK("Fill in the blank", Icons.Filled.Edit),
}