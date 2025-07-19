package com.example.mychattingapp.widgets.ChatScreenWidgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mychattingapp.FireBaseLogics.addMessageToFirestore
import com.example.mychattingapp.FireBaseLogics.addMessageToFirestoreMeta
import com.example.mychattingapp.FireBaseLogics.deleteMessageFromFirestore
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.LocalMessage
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.Message
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.Media.MediaNames.MediaTypes
import com.example.mychattingapp.Utils.DateUtils.convertToGMT
import com.example.mychattingapp.widgets.HomeScreenWidgets.TopBarFun
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons


@Composable
fun MessageSelectTopBar(
    selectedChats: String = "1",
    viewModel: ChatAppViewModel,
    messageList: List<LocalMessage>,
    onDismiss: () -> Unit
) {
    val showDeleteMessageDialogue = remember {
        mutableStateOf(false)
    }

    val isMessegeSelectDropDown = remember {
        mutableStateOf(false)
    }
    TopBarFun(
        firstIcon = {
            IconButton(
                onClick = {
                    viewModel.clearSelectedMessages()
                    viewModel.isEditingInitiated(false)

                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        text = selectedChats,
        color = MaterialTheme.colorScheme.background,
        body = {
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                IconButton(
                    onClick = {}

                ) {
                    FaIcon(
                        faIcon = FaIcons.ArrowAltCircleLeftRegular,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                IconButton(
                    onClick = {}

                ) {
                    FaIcon(
                        faIcon = FaIcons.Save,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                IconButton(
                    onClick = {
                        showDeleteMessageDialogue.value = true
                    }

                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",

                        )
                }


                ShowDeleteMessageDialogue(
                    showDialog = showDeleteMessageDialogue,
                    viewModel = viewModel,
                    messageList = messageList,
                    onDismiss = onDismiss
                )

                IconButton(
                    onClick = {}

                ) {
                    FaIcon(
                        faIcon = FaIcons.ArrowAltCircleRight,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                IconButton(
                    onClick = {
                        isMessegeSelectDropDown.value = true
                    }

                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = ""
                    )
                    MessegeSelectDropDownMenu(
                        expanded = isMessegeSelectDropDown,
                        viewModel = viewModel
                    )
                }

            }

        }
    )
}

@Composable
private fun ShowDeleteMessageDialogue(
    showDialog: MutableState<Boolean>,
    viewModel: ChatAppViewModel,
    messageList: List<LocalMessage>,
    onDismiss: () -> Unit
) {
    val auth = Firebase.auth
    if (showDialog.value) {
        Dialog(onDismissRequest = {
            showDialog.value = false
            onDismiss()
        }) {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .width(300.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.background,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(text = "Want to delete?")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                        for (message in messageList) {
                            addMessageToFirestoreMeta(
                                viewModel = viewModel,
                                message = Message(
                                    id = -1,
                                    deletedForEveryone = message.messageId,
                                    text = "deleted message",
                                    icons = "clock",
                                    password = "",
                                    reaction = "",
                                    viewOnce = "",
                                    chatId = -1,
                                    sender = message.sender,
                                    receiver = message.receiver,
                                    timestamp = convertToGMT(message.timestamp),
                                    editIcon = "",
                                    fileType = MediaTypes.TXT.name
                                )
                            )
                            viewModel.deleteMessage(message)
                            deleteMessageFromFirestore(message.id)
                        }
                        showDialog.value = false
                        viewModel.clearSelectedMessages()
                        onDismiss()
                    }) {
                        Text("Delete for everyone")
                    }
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                        for (message in messageList) {
                            val updateDeletedmessage = mapOf(
                                "deletedForMe" to (auth.currentUser?.uid ?: "")
                            )
                            viewModel.updateMessageItem(message.id, updateDeletedmessage)

                            viewModel.deleteMessage(message)
                        }
                        showDialog.value = false
                        viewModel.clearSelectedMessages()
                        onDismiss()
                    }) {
                        Text("Delete for me")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                        showDialog.value = false
                        onDismiss()
                    }) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}