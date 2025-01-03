package pt.isec.amov.quizec.ui.screens.auth

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import pt.isec.amov.quizec.ui.screens.BottomNavBarItem

//sealed class BottomNavBarItem(
//    var title: String,
//    var icon: ImageVector
//) {
//    object Home :
//        BottomNavBarItem(
//            "Home",
//            Icons.Filled.Home
//        )
//
//    object Quiz :
//        BottomNavBarItem(
//            "Quiz",
//            Icons.Filled.ContentPaste
//        )
//
//    object Question :
//        BottomNavBarItem(
//            "Question",
//            Icons.Filled.Checklist
//        )
//    object History :
//        BottomNavBarItem(
//            "History",
//            Icons.Filled.History
//        )
//    object Logout :
//        BottomNavBarItem(
//            "Logout",
//            Icons.AutoMirrored.Filled.Logout
//        )
//}
//
//@Composable
//fun NavBar() {
//    var selectedItem by remember { mutableStateOf<BottomNavBarItem>(BottomNavBarItem.Home) }
//
//    val items = listOf(
//        BottomNavBarItem.Home,
//        BottomNavBarItem.Quiz,
//        BottomNavBarItem.Question,
//        BottomNavBarItem.History,
//        BottomNavBarItem.Logout
//    )
//
//    NavigationBar {
//        items.forEach { item ->
//            AddItem(
//                screen = item,
//                isSelected = item == selectedItem,
//                onClick = { selectedItem = item }
//            )
//        }
//    }
//}

@Composable
fun BottomNavBar(
    items: List<BottomNavBarItem>,
    currentScreen: String?,
    onItemSelected: (BottomNavBarItem) -> Unit
) {
    NavigationBar {
        items.forEach { item ->
            val isSelected = currentScreen == item.title // Determine if the item is selected

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(item.title)
                }
            )
        }
    }
}


@Composable
fun RowScope.AddItem(
    screen: BottomNavBarItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.title
            )
        },
        selected = isSelected,
        alwaysShowLabel = true,
        onClick = { /*TF: TODO*/ },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = if (isSelected) Color.Blue else Color.Gray,
            selectedTextColor = if (isSelected) Color.Blue else Color.Gray,
            unselectedIconColor = Color.Gray,
            unselectedTextColor = Color.Gray
        )
    )
}
