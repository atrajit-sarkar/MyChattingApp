package com.example.mychattingapp.widgets.ChatScreenWidgets

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.mychattingapp.FireBaseLogics.Utils.MessageStates
import com.example.mychattingapp.FireBaseLogics.Utils.generateEnhancedUUID
import com.example.mychattingapp.FireBaseLogics.addMessageToFirestore
import com.example.mychattingapp.FireBaseLogics.addMessageToFirestoreMeta
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.Message
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.Media.Audio.AudioRecorder
import com.example.mychattingapp.Media.MediaNames.MediaTypes
import com.example.mychattingapp.ProfilePicUploadLogics.uploadToGitHub
import com.example.mychattingapp.Screens.getFileName
import com.example.mychattingapp.Utils.DateUtils.convertToGMT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@OptIn(ExperimentalFoundationApi::class, DelicateCoroutinesApi::class)
@Composable
fun RowScope.Send_MicButtonTransition(
    textFiledValue: MutableState<String>,
    viewModel: ChatAppViewModel,
    sendIcon: MutableState<Boolean>,
    MicIcon: MutableState<Boolean>,
    uid: String
) {
    val isEditingInitiated by viewModel.messageEditingInitiated.collectAsState()
    val selectedMessages by viewModel.selectedMessages.collectAsState()
    val isEditingGoingOn = remember { mutableStateOf(false) }
    val auth: FirebaseAuth = Firebase.auth

    // Handle message editing logic
    if (isEditingInitiated) {
        textFiledValue.value = selectedMessages[0].text
        viewModel.isEditingInitiated(false)
        isEditingGoingOn.value = true
    }
    if (selectedMessages.isEmpty()) {
        isEditingGoingOn.value = false
    }

    val sendMessageDropDownExpanded = remember { mutableStateOf(false) }
    val password = remember { mutableStateOf("") }
    val viewOnce = remember { mutableStateOf("") }
    val play = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Audio recording permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    val isRecording = remember { mutableStateOf(false) }
    val audioRecorder = remember { AudioRecorder(context){} }
    val recordedAudio = remember { mutableStateOf<File?>(null) }
    val audioFileUri = remember { mutableStateOf("") }
    val isImageSelected = viewModel.isImageSelected.collectAsState()
    val imageFileUri = remember { mutableStateOf("") }
    val uri by viewModel.tempImageLocalUri.collectAsState()
    val contentResolver = context.contentResolver

    Card(
        colors = CardDefaults.cardColors(Color(0xFF1DA81D)),
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .combinedClickable(
                onDoubleClick = {
                    if (!sendIcon.value) {
                        isRecording.value = true
                        audioRecorder.startRecording()
                    }
                },
                onLongClick = {
                    if (sendIcon.value) {
                        sendMessageDropDownExpanded.value = true
                    }
                },
                onClick = {
                    val updateOnline = mapOf(
                        "activeStatus" to "Online"
                    )
                    viewModel.updateUserItem(auth.currentUser?.uid.toString(), updateOnline)
                    play.value = true
                    val currentTime = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                    )
                        .apply {
                            timeZone = TimeZone.getTimeZone("GMT")
                        }
                        .format(Date())

                    if (textFiledValue.value.isNotBlank() || isImageSelected.value) {
                        if (textFiledValue.value.isNotBlank()) {
                            if (!isEditingGoingOn.value) {
                                auth.currentUser?.uid
                                    ?.let {
                                        if (uid != it) {
                                            Message(
                                                sender = it,
                                                text = textFiledValue.value,
                                                timestamp = currentTime,
                                                chatId = -1,
                                                receiver = uid,
                                                reaction = "",
                                                icons = "clock",
                                                password = password.value,
                                                viewOnce = viewOnce.value,
                                                deletedForMe = "",
                                                deletedForEveryone = generateEnhancedUUID()
                                                    .toString(),
                                                fileType = MediaTypes.TXT.name
                                            )
                                        } else {
                                            Message(
                                                sender = it,
                                                text = textFiledValue.value,
                                                timestamp = currentTime,
                                                chatId = -1,
                                                receiver = uid,
                                                reaction = "",
                                                icons = "doubleTickGreen",
                                                password = password.value,
                                                viewOnce = viewOnce.value,
                                                deletedForMe = "",
                                                deletedForEveryone = generateEnhancedUUID()
                                                    .toString(),
                                                fileType = MediaTypes.TXT.name
                                            )
                                        }
                                    }
                                    ?.let {
                                        addMessageToFirestore(it, viewModel)
                                    }
                            } else {
                                val selectedMessage = Message(
                                    sender = selectedMessages[0].sender,
                                    text = textFiledValue.value,
                                    timestamp = convertToGMT(selectedMessages[0].timestamp),
                                    chatId = -1,
                                    receiver = selectedMessages[0].receiver,
                                    reaction = selectedMessages[0].reaction,
                                    icons = "clock",
                                    password = selectedMessages[0].password,
                                    viewOnce = selectedMessages[0].viewOnce,
                                    deletedForMe = "",
                                    deletedForEveryone = selectedMessages[0].messageId,
                                    editIcon = MessageStates.Edited.name
                                )
                                addMessageToFirestoreMeta(selectedMessage, viewModel)
//                            viewModel.updateMessageItem(selectedMessages[0].id, updates)
                                viewModel.clearSelectedMessages()
                            }

                            textFiledValue.value = "" // Clear the input

                        } else if (isImageSelected.value) {
                            viewModel.changeUploadingProfilePhotoState(true)
                            imageFileUri.value = generateEnhancedUUID()
                                .toString()

                            // Open and copy the file to cache asynchronously
                            GlobalScope.launch(Dispatchers.IO) {
                                val selectedFile = uri.toUri().let { uri ->
                                    contentResolver.openInputStream(uri)
                                        ?.use { inputStream ->
                                            val fileName =
                                                getFileName(context, uri) ?: "default_name"
                                            val tempFile = File(context.cacheDir, fileName)
                                            tempFile.outputStream().use { outputStream ->
                                                inputStream.copyTo(outputStream)
                                            }
                                            tempFile // Return the File object
                                        }
                                }

                                delay(2000)  // Adjust the delay as necessary

                                // Proceed with uploading the file to GitHub only if file is not null
                                selectedFile?.let { file ->
                                    uploadToGitHub(
                                        token = "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",
                                        owner = "gongobongofounder",
                                        repo = "MCA_ImageRepo",
                                        filePath = "${imageFileUri.value}.jpg",
                                        file = file,
                                        viewModel = viewModel
                                    )
                                }
                            }
                            auth.currentUser?.uid
                                ?.let {
                                    if (uid != it) {
                                        Message(
                                            sender = it,
                                            text = textFiledValue.value,
                                            timestamp = currentTime,
                                            chatId = -1,
                                            receiver = uid,
                                            reaction = "",
                                            icons = "clock",
                                            password = password.value,
                                            viewOnce = viewOnce.value,
                                            deletedForMe = "",
                                            deletedForEveryone = generateEnhancedUUID()
                                                .toString(),
                                            fileType = MediaTypes.IMAGE.name,
                                            fileUri =imageFileUri.value
                                        )
                                    } else {
                                        Message(
                                            sender = it,
                                            text = textFiledValue.value,
                                            timestamp = currentTime,
                                            chatId = -1,
                                            receiver = uid,
                                            reaction = "",
                                            icons = "doubleTickGreen",
                                            password = password.value,
                                            viewOnce = viewOnce.value,
                                            deletedForMe = "",
                                            deletedForEveryone = generateEnhancedUUID()
                                                .toString(),
                                            fileType = MediaTypes.IMAGE.name,
                                            fileUri = imageFileUri.value
                                        )
                                    }
                                }
                                ?.let {
                                    addMessageToFirestore(it, viewModel)
                                }
                        }
                    } else {
                        audioFileUri.value = generateEnhancedUUID().toString()
                        isRecording.value = false
                        recordedAudio.value = audioRecorder.stopRecording()
                        recordedAudio.value?.let { file ->
                            viewModel.changeUploadingProfilePhotoState(true)
                            uploadToGitHub(
                                token = "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",
                                owner = "gongobongofounder",
                                repo = "MCA_AudioRepo",
                                filePath = "${audioFileUri.value}.mp3",
                                file = file,
                                viewModel = viewModel
                            )
                        }

                        auth.currentUser?.uid
                            ?.let {
                                if (uid != it) {
                                    Message(
                                        sender = it,
                                        text = textFiledValue.value,
                                        timestamp = currentTime,
                                        chatId = -1,
                                        receiver = uid,
                                        reaction = "",
                                        icons = "clock",
                                        password = password.value,
                                        viewOnce = viewOnce.value,
                                        deletedForMe = "",
                                        deletedForEveryone = generateEnhancedUUID()
                                            .toString(),
                                        fileType = MediaTypes.AUDIO.name,
                                        fileUri = audioFileUri.value
                                    )
                                } else {
                                    Message(
                                        sender = it,
                                        text = textFiledValue.value,
                                        timestamp = currentTime,
                                        chatId = -1,
                                        receiver = uid,
                                        reaction = "",
                                        icons = "doubleTickGreen",
                                        password = password.value,
                                        viewOnce = viewOnce.value,
                                        deletedForMe = "",
                                        deletedForEveryone = generateEnhancedUUID()
                                            .toString(),
                                        fileUri = audioFileUri.value,
                                        fileType = MediaTypes.AUDIO.name
                                    )
                                }
                            }
                            ?.let {
                                if (recordedAudio.value != null) {
                                    addMessageToFirestore(it, viewModel)
                                }
                            }


                    }

                    password.value = ""
                    viewOnce.value = ""
                }
            ),
        content = {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                AnimatedContent(
                    targetState = sendIcon.value,
                    transitionSpec = {
                        (fadeIn(tween(300)) + scaleIn(
                            initialScale = 0.7f,
                            animationSpec = tween(300)
                        )).togetherWith(
                            fadeOut(tween(200)) + scaleOut(
                                targetScale = 0.7f,
                                animationSpec = tween(200)
                            )
                        )

                    }
                ) { showSendIcon ->
                    if (showSendIcon) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(35.dp)
                                .padding(horizontal = 4.dp)
                        )
                    } else {
                        FaIcon(
                            faIcon = FaIcons.Microphone,
                            tint = Color.Black,
                            modifier = Modifier
                                .size(24.dp)
                                .padding(horizontal = 4.dp)
                        )
                    }
                }


            }
        }
    )



    DropDownMenuSendMessage(
        expanded = sendMessageDropDownExpanded,
        viewModel = viewModel,
        password = password,
        viewOnce = viewOnce
    )
}
