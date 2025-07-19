package com.example.mychattingapp.widgets.ChatScreenWidgets

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.NavHost.navigateIfNotFast
import com.example.mychattingapp.widgets.HomeScreenWidgets.AddUserUI
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@Composable
fun DropDownMenuSendMessage(
    expanded: MutableState<Boolean>,
    navController: NavController = rememberNavController(),
    viewModel: ChatAppViewModel,
    password: MutableState<String> = remember { mutableStateOf("") },
    viewOnce: MutableState<String> = remember { mutableStateOf("") }

) {
    val showDialogue = remember { mutableStateOf(false) }
    val showAddUser = remember { mutableStateOf(false) }

    PasswordDialog(password = password, showDialog = showDialogue)
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        shadowElevation = 10.dp,
        tonalElevation = 10.dp,
        offset = DpOffset(x = 150.dp, y = 20.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(180.dp)
    ) {

        DropdownMenuItem(
            onClick = { /* Handle option 2 click */
                showDialogue.value = true
                expanded.value = false

            },
            text = {
                Text("Password Protect")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.UserSecret,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = {/* Handle option 2 click */
                viewOnce.value="closed"
                expanded.value = false
            },
            text = {
                Text("ViewOnce")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.Lock,
                    tint = Color.LightGray
                )
            }
        )
    }
}