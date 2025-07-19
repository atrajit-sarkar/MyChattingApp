package com.example.mychattingapp.widgets.HomeScreenWidgets

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.User
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel

@Composable
fun DeleteUserDialogue(
    showDialog: MutableState<Boolean>,
    contactName: String,
    viewModel: ChatAppViewModel,
    user: User
) {

    val messageList by viewModel.getMessageById(user.uid).observeAsState(emptyList())
    val userDocListGlobal by viewModel.userDocIdListGlobal.observeAsState(emptyList())

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
                    text = "Want to delete $contactName",
                    fontSize = 25.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        viewModel.deleteUserDocId(userdocid = user.uid)


                        showDialog.value = false

                    }
                ) {
                    Text("Yes")
                }
                Button(
                    onClick = {
                        showDialog.value = false

                    }
                ) {
                    Text("No")
                }
            }
        }
    }
}
