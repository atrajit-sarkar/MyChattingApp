package com.example.mychattingapp.Screens

import androidx.compose.ui.window.Dialog
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.NavHost.navigateIfNotFast
import com.example.mychattingapp.R
import com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme.ProfessionalDoodleBackground
import com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme.WallPaperNames
import com.example.mychattingapp.ui.theme.MyChattingAppTheme
import com.example.mychattingapp.widgets.ChatScreenWidgets.ChatInputField
import com.example.mychattingapp.widgets.ChatScreenWidgets.ChatScreenTopBar
import com.example.mychattingapp.widgets.ChatScreenWidgets.MessageSelectTopBar
import com.example.mychattingapp.widgets.ChatScreenWidgets.MessageViewWindow
import com.example.mychattingapp.widgets.SoundWidgets.PlaySound
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun ChatScreen(
    viewModel: ChatAppViewModel,
    lastSeen: String = "12:00 PM",
    navController: NavController = rememberNavController(),
    uid: String,
    messageId: Int = -1
) {


    viewModel.changeUid(uid)
    val textFiledValue = remember { mutableStateOf("") }
    val currentUser = viewModel.filterUser(uid).collectAsState()
//    val messageList by viewModel.filterTargetMessages(uid).collectAsState(emptyList())
    val messageList by viewModel.getMessageById(uid).observeAsState(emptyList())
    val isUserTyping by viewModel.userTyping.collectAsState()
    val userTyping = remember { mutableStateOf(isUserTyping) }


    val sendIcon = remember { mutableStateOf(false) }
    val MicIcon = remember { mutableStateOf(false) }

    // Message delete implementation...........
    val selectedMessages by viewModel.selectedMessages.collectAsState()
    Log.d("SelectedMessages", "ChatScreen: $selectedMessages")

    if (selectedMessages.isEmpty()) {
        viewModel.isMessageSelectInitiated(false)
    }

    // Initialize LazyListState with the last item index
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = if (messageList.isNotEmpty()) messageList.size - 1 else 0
    )
    val coroutineScope = rememberCoroutineScope()


    val showScrollToBottomFab by remember {
        derivedStateOf {
            val lastIndex = messageList.lastIndex
            val visibleItemsInfo = listState.layoutInfo.visibleItemsInfo
            // Check if the last visible item is not the last message in the list
            if (visibleItemsInfo.isNotEmpty()) {
                val lastVisibleItemIndex = visibleItemsInfo.last().index
                lastVisibleItemIndex < lastIndex
            } else {
                false
            }
        }
    }
    val isOwnMessage = remember { mutableStateOf(true) }
    val loggedInUserId by viewModel.currentUserId.collectAsState()


    val currentTime = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        Locale.getDefault()
    ).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }.format(Date())

    val chattingToUpdate = mapOf(
        "chattingTo" to uid
    )
    viewModel.updateUserItem(loggedInUserId, chattingToUpdate)

    if (currentUser.value[0].activeStatus == "Typing...." && currentUser.value[0].chattingTo == loggedInUserId) {
        viewModel.changeUserTyping(true)
    } else {
        viewModel.changeUserTyping(false)

    }

    val isPlaying by viewModel.isPlaying.collectAsState()
    val isPlayingReceived by viewModel.isPlayingReceived.collectAsState()

    if (isPlaying) {
        PlaySound(R.raw.send)
        // Adding a delay before changing the state to false
        LaunchedEffect(Unit) {
            delay(200)
            viewModel.changeIsPlaying(false) // Change state after the delay
        }
    } else if (isPlayingReceived) {

        PlaySound(R.raw.receive)
        LaunchedEffect(Unit) {
            delay(300)
            viewModel.changeIsPlayingReceived(false) // Change state after the delay
        }
    }

    val deleteAllMessageState by viewModel.deleteMessageFromFirestore.collectAsState()



    MyChattingAppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            BackgroundImage(viewModel)



            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.2f))
            )

            Scaffold(floatingActionButton = {
                AnimatedVisibility(visible = deleteAllMessageState) {
                    Dialog(
                        onDismissRequest = {}

                    ) {
                        Surface(
                            modifier = Modifier.padding(16.dp),
                            shape = MaterialTheme.shapes.large,
                            color = Color.Transparent,
                            tonalElevation = 8.dp
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                AnimatedVisibility(visible = showScrollToBottomFab) {

                    Column {

                        FloatingActionButton(containerColor = MaterialTheme.colorScheme.background,
                            shape = CircleShape,
                            modifier = Modifier.size(35.dp),
                            onClick = {
                                coroutineScope.launch {
                                    listState.scrollToItem(0)
                                }
                            }) {
                            FaIcon(
                                faIcon = FaIcons.AngleDoubleUp,
                                tint = MaterialTheme.colorScheme.onSurface,
                                size = 20.dp,
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        FloatingActionButton(containerColor = MaterialTheme.colorScheme.background,
                            shape = CircleShape,
                            modifier = Modifier.size(35.dp),
                            onClick = {
                                coroutineScope.launch {
                                    listState.scrollToItem(messageList.lastIndex)
                                }
                            }) {
                            FaIcon(
                                faIcon = FaIcons.AngleDoubleDown,
                                tint = MaterialTheme.colorScheme.onSurface,
                                size = 20.dp,
                            )
                        }
                    }
                }
            },

                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.ime)
                    .background(Color.Transparent), // Ensure dark background
                topBar = {

                    currentUser.value[0].let {
                        ChatScreenTopBar(
                            navController, it.userName, it.activeStatus, viewModel, user = it
                        )
                    }
                    AnimatedVisibility(visible = selectedMessages.isNotEmpty()) {
                        MessageSelectTopBar(selectedChats = "${selectedMessages.size}",
                            viewModel = viewModel,
                            messageList = selectedMessages,
                            onDismiss = { viewModel.clearSelectedMessages() })
                    }

                }, bottomBar = {
                    ChatInputField(textFiledValue, sendIcon, MicIcon, viewModel, uid)
                }, containerColor = Color.Transparent
            ) { innerPadding ->

                MessageViewWindow(
                    innerPadding = innerPadding,
                    messageList = messageList,
                    viewModel = viewModel,
                    messageId = messageId,
                    listState = listState
                )


            }
        }
    }
}


@Composable
fun BackgroundImage(viewModel: ChatAppViewModel) {
    val id by viewModel.wallPaperId.collectAsState()

    Image(
        painter = painterResource(id = id), // Replace with your image resource
        contentDescription = null, // Decorative image
        modifier = Modifier
            .fillMaxSize()
            .blur(radius = 3.dp),
        contentScale = ContentScale.Crop, // Ensures the image covers the entire screen
    )
}





