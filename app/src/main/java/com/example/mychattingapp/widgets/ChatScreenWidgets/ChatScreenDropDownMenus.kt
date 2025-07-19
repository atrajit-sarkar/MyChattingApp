package com.example.mychattingapp.widgets.ChatScreenWidgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mychattingapp.FireBaseLogics.deleteAllMessageOfUid
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.User
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.NavHost.navigateIfNotFast
import com.example.mychattingapp.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@Composable
fun DropDownMenuChatScreen(
    expanded: MutableState<Boolean>,
    navController: NavController = rememberNavController(),
    subexpanded: MutableState<Boolean>

) {
    val showAddUserUI = remember { mutableStateOf(false) }
    val textFieldValue = remember { mutableStateOf("") }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        shadowElevation = 10.dp,
        tonalElevation = 10.dp,
        offset = DpOffset(x = 10.dp, y = 15.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(220.dp)
    ) {

        DropdownMenuItem(
            onClick = { /* Handle option 2 click */
                showAddUserUI.value = true
                expanded.value = false

            },
            text = {
                Text("View contacts")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.AddressCard,
                    tint = Color.LightGray
                )
            }
        )


        DropdownMenuItem(
            onClick = { /* Handle option 3 click */ },
            text = {
                Text("Search")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.Search,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = { /* Handle option 3 click */ },
            text = {
                Text("Add to list")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.ClipboardList,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = { /* Handle option 3 click */ },
            text = {
                Text("Media, links, and docs")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.FileMedicalAlt,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = { /* Handle option 3 click */ },
            text = {
                Text("Mute notifications")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.VolumeMute,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = { /* Handle option 1 click */

            },
            text = {
                Text("Disappearing messages")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.Clock,
                    tint = Color.LightGray
                )
            }
        )

        DropdownMenuItem(
            onClick = {
                expanded.value = false
                navigateIfNotFast {
                    navController.navigate("chat_theme_screen")
                }
            },
            text = {
                Text("Chat theme")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.Affiliatetheme,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = {
                subexpanded.value = true
                expanded.value = false

            },
            text = {
                Text("More")
            },
            trailingIcon = {
                FaIcon(
                    faIcon = FaIcons.ArrowRight,
                    tint = Color.LightGray
                )
            }
        )


    }

}

@Composable
fun SubDropDownMenuChatScreen(
    subexpanded: MutableState<Boolean>,
    clearChatDialog: MutableState<Boolean>
) {

    DropdownMenu(
        expanded = subexpanded.value,
        onDismissRequest = { subexpanded.value = false },
        shadowElevation = 10.dp,
        tonalElevation = 10.dp,
        offset = DpOffset(x = 10.dp, y = 15.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(180.dp)
    ) {

        DropdownMenuItem(
            onClick = { /* Handle option 2 click */


            },
            text = {
                Text("Report")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.ThumbsDown,
                    tint = Color.LightGray
                )
            }
        )


        DropdownMenuItem(
            onClick = { /* Handle option 3 click */ },
            text = {
                Text("Block")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.Cross,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = { // Clear Chat
                clearChatDialog.value = true
                subexpanded.value = false


            },
            text = {
                Text("Clear chat")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.MinusCircle,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = { /* Handle option 3 click */ },
            text = {
                Text("Export Chat")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.FileExport,
                    tint = Color.LightGray
                )
            }
        )
        DropdownMenuItem(
            onClick = { /* Handle option 3 click */ },
            text = {
                Text("Add shortcut")
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.UserAlt,
                    tint = Color.LightGray
                )
            }
        )


    }
}

@Composable
fun ClearChatDialogue(
    showDialog: MutableState<Boolean>,
    viewModel: ChatAppViewModel,
    user: User
) {
    val messageList by viewModel.getMessageById(user.uid).observeAsState(emptyList())
    val auth = Firebase.auth

    Dialog(onDismissRequest = { showDialog.value = false }) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Clear this chat?",
                    fontSize = 25.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.SpaceBetween) {

                    TextButton(
                        onClick = {
                            showDialog.value = false

                        }
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(50.dp))
                    TextButton(
                        onClick = {
                            val updateDeletedmessage = mapOf(
                                "deletedForMe" to (auth.currentUser?.uid ?: "")
                            )

                            for (message in messageList) {

                                viewModel.updateMessageItem(message.id, updateDeletedmessage)

                            }
                            viewModel.changeDeleteMessageFromFirestore(true)
                            viewModel.deleteAllMessage(user.uid)

                            showDialog.value = false

                        }
                    ) {
                        Text("Clear chat")
                    }
                }

            }
        }
    }
}