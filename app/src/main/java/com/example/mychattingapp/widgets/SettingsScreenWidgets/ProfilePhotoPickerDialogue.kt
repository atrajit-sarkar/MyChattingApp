package com.example.mychattingapp.widgets.SettingsScreenWidgets

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePicPickerBottomSheet(
    showBottomSheet: MutableState<Boolean>,
    viewModel: ChatAppViewModel,
    onProfilePicSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { onProfilePicSelected(it) }
            showBottomSheet.value = false
        }

    ModalBottomSheet(
        onDismissRequest = { showBottomSheet.value = false }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            TextButton(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
                    .align(alignment = Alignment.Start)
            ) {
                Text("Select from Gallery")
            }
            TextButton(
                onClick = {
                    // Placeholder for remove profile photo logic
                    val profilePicUpdate = mapOf<String, Any>(
                        "profilePicUri" to ""
                    )
                    viewModel.updateUserItem(
                        viewModel.currentUserId.value,
                        profilePicUpdate
                    )
                    Toast.makeText(
                        context,
                        "Removed ProfilePhoto",
                        Toast.LENGTH_SHORT
                    ).show()
                    showBottomSheet.value = false
                },
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.Start)
            ) {
                Text("Remove Profile Photo")
            }
        }
    }
}