package com.example.mychattingapp.widgets.ChatScreenWidgets

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.LocalMessage
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.Message

@Composable
fun PasswordDialog(
    showDialog: MutableState<Boolean>,
    password: MutableState<String>,
    message: LocalMessage = LocalMessage(
        sender = "GB Founder",
        text = "Fuck off",
        timestamp = "69:69",
        reaction = ""
    )
) {

    val tempPassword = remember { mutableStateOf("") }
    val context = LocalContext.current

    if (showDialog.value) {
        // Dialog for password input
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .width(300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Enter Password",
                        style = MaterialTheme.typography.titleMedium
                    )
                    // Password TextField
                    OutlinedTextField(
                        value = tempPassword.value,
                        onValueChange = { tempPassword.value = it },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            showDialog.value = false
                            tempPassword.value = ""
                        }) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = {
                                showDialog.value = false
                                // Handle the password (e.g., validation logic)
                                password.value = tempPassword.value
                                tempPassword.value = ""
                                if ( message.password!= "" && password.value != message.password) {
                                    Toast.makeText(
                                        context,
                                        "Incorrect Password",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            },
                            enabled = tempPassword.value.isNotEmpty()
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}
