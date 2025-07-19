package com.example.mychattingapp.widgets.HomeScreenWidgets

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mychattingapp.FireBaseLogics.addUserToFirestore
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.User
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.UserDocId
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel

@Composable
fun AddUserUI(
    showDialog: MutableState<Boolean>,
    textFieldValue: MutableState<String>,
    viewModel: ChatAppViewModel
) {
    val newUser = User(
        userName = textFieldValue.value,
        messageSentTime = "",
        messageCounter = "",
        recentMessage = "",
        password = ""
    )
//    val userDocIds by viewModel.userDocIds.observeAsState()

    val isError = remember { mutableStateOf(false) }

    Dialog(onDismissRequest = {
        showDialog.value = false
        textFieldValue.value = ""
    }) {
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
                    text = "Add contact Details below:",
                    fontSize = 25.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    isError = isError.value,
                    enabled = !isError.value,
                    value = textFieldValue.value,
                    onValueChange = {
                        textFieldValue.value = it
                        isError.value = false
                    },
                    label = {
                        Text("Add Contact")
                    },
                    placeholder = {
                        Text("Enter contact DocId")
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        // Add the new user ID only if it doesn't already exist
                        if (viewModel.userDocId.value.contains(textFieldValue.value)) {
                            isError.value = true
                        } else {
                            val userDocId = UserDocId(userdocid = textFieldValue.value)
                            viewModel.addUserDocId(userDocId = userDocId)
                            showDialog.value = false
                            textFieldValue.value = ""
                            Log.d("UserIds", "AddUserUI: ${viewModel.userDocId.value}")
//                            viewModel.fetchUsersInRealTime()
                        }
                    }
                ) {
                    Text("ADD")
                }
            }
        }
    }
}