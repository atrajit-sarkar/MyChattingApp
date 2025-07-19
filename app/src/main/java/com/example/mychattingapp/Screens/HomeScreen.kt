package com.example.mychattingapp.Screens

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Space
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.LocalMessage
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.Message
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.User
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.NavHost.navigateIfNotFast
import com.example.mychattingapp.RotatingIcon
import com.example.mychattingapp.ui.theme.MyChattingAppTheme
import com.example.mychattingapp.widgets.HomeScreenWidgets.ChatRow
import com.example.mychattingapp.widgets.HomeScreenWidgets.DeleteUserDialogue
import com.example.mychattingapp.widgets.HomeScreenWidgets.DropDownMenuHomeScreen
import com.example.mychattingapp.widgets.HomeScreenWidgets.TopBarFun
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.system.exitProcess

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
//@Preview
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    viewModel: ChatAppViewModel
) {
    val messageCounterEnabled = remember { mutableStateOf(true) }
    val showDeleteUserDialoge = remember {
        mutableStateOf(false)
    }
    val selectedContact = remember { mutableStateOf<User?>(null) }

    // ChatAppViewModel Variables.......
    val userList by viewModel.userList.collectAsState(emptyList())
    val messageList by viewModel.allLocalChats.observeAsState(emptyList())
    val searchinputtext = remember { mutableStateOf("") }
    val isFocused by viewModel.homeScreenSearchBarActiveState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val userShortedList = remember {
        mutableStateListOf<User>()
    }
    val messageShortedList = remember {
        mutableStateListOf<LocalMessage>()
    }
    val auth = Firebase.auth


    // Clear the lists before adding new filtered results
    userShortedList.clear()
    for (user in userList) {
        if (user.userName.contains(searchinputtext.value, ignoreCase = true)) {
            userShortedList.add(user)
        }
    }

    messageShortedList.clear()
    for (chat in messageList) {
        if (chat.text.contains(searchinputtext.value, ignoreCase = true)) {
            messageShortedList.add(chat)
        }
    }

    viewModel.changeUid("")
    viewModel.changeIsPlaying(false)
    viewModel.changeIsPlayingReceived(false)
    val chattingTo = mapOf(
        "chattingTo" to ""
    )
    viewModel.updateUserItem(auth.currentUser?.uid ?: "", chattingTo)
    val showProfilePicDialogues = remember {
        mutableStateOf(false)
    }
    viewModel.changeTempImageLocalUri("")
    viewModel.changeIsImageSelected(false)


    MyChattingAppTheme {

        LazyColumn(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight()
        ) {

            if (!isFocused) {

                item {

                    SearchBar(searchinputtext, focusRequester, viewModel, isFocused, focusManager)
                }
            } else {
                stickyHeader {
                    SearchBar(searchinputtext, focusRequester, viewModel, isFocused, focusManager)
                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }
                }
            }


            if (isFocused && searchinputtext.value != "") {
                item {
                    LazyRow(horizontalArrangement = Arrangement.SpaceEvenly) {
                        item {

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = MaterialTheme.colorScheme.background,
                                modifier = Modifier.padding(5.dp)
                            ) {
                                Row(modifier = Modifier.padding(8.dp)) {
                                    FaIcon(
                                        faIcon = FaIcons.Link,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        size = 20.dp
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text("Links")
                                }
                            }
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = MaterialTheme.colorScheme.background,
                                modifier = Modifier.padding(5.dp)
                            ) {
                                Row(modifier = Modifier.padding(8.dp)) {
                                    FaIcon(
                                        faIcon = FaIcons.Image,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        size = 20.dp
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text("Photos")
                                }
                            }
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = MaterialTheme.colorScheme.background,
                                modifier = Modifier.padding(5.dp)
                            ) {
                                Row(modifier = Modifier.padding(8.dp)) {
                                    FaIcon(
                                        faIcon = FaIcons.File,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        size = 20.dp
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text("Documents")
                                }
                            }
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = MaterialTheme.colorScheme.background,
                                modifier = Modifier.padding(5.dp)
                            ) {
                                Row(modifier = Modifier.padding(8.dp)) {
                                    FaIcon(
                                        faIcon = FaIcons.Video,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        size = 20.dp
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text("Videos")
                                }
                            }
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = MaterialTheme.colorScheme.background,
                                modifier = Modifier.padding(5.dp)
                            ) {
                                Row(modifier = Modifier.padding(8.dp)) {
                                    FaIcon(
                                        faIcon = FaIcons.Poll,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        size = 20.dp
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text("Polls")
                                }
                            }
                        }

                    }
                }

                item {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Chats",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                items(userShortedList) { contact ->
                    ChatRow(
                        contactName = contact.userName,
                        recentMessage = contact.recentMessage,
                        messageSentTime = contact.messageSentTime,
                        onClick = {
                            navigateIfNotFast {
                                navController.navigate("chat_screen/${contact.uid}")
                                viewModel.changeIsPlaying(false)
                                viewModel.changeIsPlayingReceived(false)

                            }
                        },
                        onLongPress = {
                            // new logic will be implemented later...........

                        },
                        profilePicUri = contact.profilePicUri,
                    )


                }

                item {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Messages",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                items(messageShortedList) { message ->
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(100.dp)
                            .fillMaxWidth()
                            .clickable {
                                navigateIfNotFast {
                                    if (message.sender == viewModel.currentUserId.value) {
                                        navController.navigate("chat_screen/${message.receiver}")
                                    } else if (message.receiver == viewModel.currentUserId.value) {
                                        navController.navigate("chat_screen/${message.sender}")
                                    }
                                }


                            },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.width(200.dp)) {
                            Column {
                                Text(
                                    identifyUserFromAMessage(
                                        users = userList,
                                        message = message
                                    )
                                )
                                Text(
                                    message.text,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )

                            }

                        }
                        Spacer(modifier = Modifier.width(30.dp))
                        Row {
                            Text(
                                message.timestamp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                        }

                    }

                }
            } else {

                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())


                items(userList) { contact ->
                    val messages = viewModel.getMessageById(uid = contact.uid)
                        .observeAsState(emptyList())
                    var messageCounter = 0
                    for (message in messages.value) {
                        if (message.sender == contact.uid && message.icons != "doubleTickGreen") {
                            messageCounter++
                        }
                    }
                    if (messages.value.isNotEmpty()) {
                        val date = inputFormat.parse(messages.value.last().timestamp)

                        if (messages.value.last().password != "" || messages.value.last().viewOnce != "") {
                            contact.recentMessage =
                                if (messages.value.last().sender == auth.currentUser?.uid) "You sent a Protect Message" else "He sent a Protect Message"
                        } else {
                            contact.recentMessage =
                                if (messages.value.last().reaction == "") messages.value.last().text else "You reacted ${messages.value.last().reaction} to \"${messages.value.last().text}\""
                        }
                        contact.messageSentTime = outputFormat.format(date!!)
                    } else {
                        contact.recentMessage = ""
                        contact.messageSentTime = ""
                    }
//                    viewModel.updateUser(user = contact)
                    val currentUserUid by viewModel.currentUserId.collectAsState()
                    ChatRow(
                        contactName = if (contact.uid == currentUserUid) "(You) ${contact.userName}" else contact.userName,
                        recentMessage = contact.recentMessage,
                        messageSentTime = contact.messageSentTime,
                        messageCounterNumber = messageCounter.toString(),
                        onClick = {
                            navigateIfNotFast {
                                navController.navigate("chat_screen/${contact.uid}")

                            }
                        },
                        onLongPress = {
                            selectedContact.value = contact
                            showDeleteUserDialoge.value = true

                        },
                        profilePicUri = contact.profilePicUri,
                    ) {
                        MessageIconsRendering(messages.value, auth)
                    }

                }
            }

            if ((userList.size > 5 && userShortedList.size > 5)) {

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = Color(0xFF77777A))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp)
                            .height(30.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FaIcon(
                            faIcon = FaIcons.LockOpen,
                            tint = MaterialTheme.colorScheme.onBackground,
                            size = 15.dp
                        )
                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            "Chats are open and leaking to our server",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        }

        if (showDeleteUserDialoge.value) {
            selectedContact.value?.let { contact ->
                DeleteUserDialogue(
                    showDialog = showDeleteUserDialoge,
                    contactName = contact.userName,
                    viewModel = viewModel,
                    user = contact
                )
            }
        }
    }


}

@Composable
private fun MessageIconsRendering(
    messages: List<LocalMessage>,
    auth: FirebaseAuth
) {
    if (messages.last().sender == auth.currentUser?.uid) {
        if (messages.last().icons == "singleTick") {
            Spacer(modifier = Modifier.width(5.dp))
            FaIcon(
                faIcon = FaIcons.Check,
                tint = Color.LightGray,
                size = 15.dp,
            )
        } else if (messages.last().icons == "clock") {
            Spacer(modifier = Modifier.width(5.dp))
            FaIcon(
                faIcon = FaIcons.Clock,
                tint = Color.LightGray,
                size = 15.dp,
            )

        } else if (messages.last().icons == "doubleTickGreen") {
            Spacer(modifier = Modifier.width(5.dp))
            FaIcon(
                faIcon = FaIcons.CheckDouble,
                tint = Color.Green,
                size = 15.dp,
            )
        } else if (messages.last().icons == "doubleTick") {
            Spacer(modifier = Modifier.width(5.dp))
            FaIcon(
                faIcon = FaIcons.CheckDouble,
                tint = Color.LightGray,
                size = 15.dp,
            )
        }
    }
}

@Composable
fun SearchBar(
    searchinputtext: MutableState<String>,
    focusRequester: FocusRequester,
    viewModel: ChatAppViewModel,
    isFocused: Boolean,
    focusManager: FocusManager
) {
    TextField(textStyle = TextStyle(fontSize = 18.sp),
        value = searchinputtext.value, onValueChange = { searchinputtext.value = it },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                viewModel.changeHomeScreenSearchBarActiveState(focusState.isFocused)
            },
        shape = RoundedCornerShape(70.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ), placeholder = { Text("ASK BAJIRAO OR SEARCH", color = Color(0xABA7ADA7)) },
        leadingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {

                if (isFocused) {
                    IconButton(onClick = {
                        focusManager.clearFocus()
                        searchinputtext.value = ""
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }

                }
                RotatingIcon()
            }
        },
        trailingIcon = {
            if (
                isFocused
            ) FaIcon(
                faIcon = FaIcons.Facebook,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun HomeTopAppBar(
    navController: NavController,
    showDropDown: MutableState<Boolean>,
    viewModel: ChatAppViewModel
) {
    TopBarFun(
        text = "Chatting App",
        firstIcon = {
            FaIcon(
                faIcon = FaIcons.Whatsapp,
                size = 35.dp,
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        color = MaterialTheme.colorScheme.background,
        navController = navController,
        body = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {

                IconButton(
                    onClick = {
                        // Drop Down Menu........
                        showDropDown.value = !showDropDown.value

                    },
                    modifier = Modifier.size(25.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }
                DropDownMenuHomeScreen(
                    expanded = showDropDown,
                    navController = navController,
                    viewModel = viewModel
                )

            }
        }
    )
}


fun identifyUserFromAMessage(
    users: List<User>,
    message: LocalMessage
): String {
    for (user in users) {
        if (user.uid == message.sender) {
            return user.userName
        }
    }
    return "" // Return an empty string if no match is found
}


@Composable
fun HomeScreenBottomBar() {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                IconButton(
                    onClick = {

                    }
                ) {

                    FaIcon(
                        faIcon = FaIcons.Rocketchat,
                        tint = Color.LightGray
                    )
                }
                Text("Chats")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                IconButton(
                    onClick = {

                    }
                ) {

                    FaIcon(
                        faIcon = FaIcons.GgCircle,
                        tint = Color.LightGray
                    )
                }
                Text("Updates")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                IconButton(
                    onClick = {

                    }
                ) {

                    FaIcon(
                        faIcon = FaIcons.PeopleArrows,
                        tint = Color.LightGray
                    )
                }
                Text("Communities")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                IconButton(
                    onClick = {

                    }
                ) {

                    FaIcon(
                        faIcon = FaIcons.Phone,
                        tint = Color.LightGray
                    )
                }
                Text("Calls")
            }

        }

    }
}

