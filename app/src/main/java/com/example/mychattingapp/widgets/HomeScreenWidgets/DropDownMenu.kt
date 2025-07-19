package com.example.mychattingapp.widgets.HomeScreenWidgets

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
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@Composable
fun DropDownMenuHomeScreen(
    expanded: MutableState<Boolean>,
    navController: NavController = rememberNavController(),
    viewModel: ChatAppViewModel

) {
    val showAddUserUI = remember { mutableStateOf(false) }
    val textFieldValue = remember { mutableStateOf("") }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        shadowElevation = 10.dp,
        tonalElevation = 10.dp,
        offset = DpOffset(x = 10.dp, y = 20.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(180.dp)
    ) {

        DropdownMenuItem(
            onClick = { /* Handle option 2 click */
                showAddUserUI.value=true
                expanded.value=false

            },
            text = {
                Text("New Group")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.UserFriends,
                    tint = Color.LightGray
                )
            }
        )


        DropdownMenuItem(
            onClick = { /* Handle option 3 click */ },
            text = {
                Text("New broadcast")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.BroadcastTower,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = { /* Handle option 3 click */ },
            text = {
                Text("Linked devices")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.Staylinked,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = { /* Handle option 3 click */ },
            text = {
                Text("Starred messages")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.Star,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = { /* Handle option 3 click */ },
            text = {
                Text("Payments")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.Wallet,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = { /* Handle option 1 click */
                navigateIfNotFast{
                    navController.navigate("settings_screen")

                }
                expanded.value=false
            },
            text = {
                Text("Settings")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
                )
            }
        )

        DropdownMenuItem(
            onClick = { /* Handle option 3 click */ },
            text = {
                Text("Switch accounts")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.ExchangeAlt,
                    tint = Color.LightGray
                )
            }
        )


    }
    if (showAddUserUI.value) {
        AddUserUI(
            showDialog = showAddUserUI,
            textFieldValue = textFieldValue,
            viewModel = viewModel
        )
    }
}