package com.example.mychattingapp.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mychattingapp.ChatsData.ChannelsData
import com.example.mychattingapp.ChatsData.UsersStatusData
import com.example.mychattingapp.ChatsData.loadChannels
import com.example.mychattingapp.ChatsData.loadStatuses
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.NavHost.navigateIfNotFast
import com.example.mychattingapp.RotatingIcon
import com.example.mychattingapp.ui.theme.MyChattingAppTheme
import com.example.mychattingapp.widgets.HomeScreenWidgets.ChatRow
import com.example.mychattingapp.widgets.HomeScreenWidgets.TopBarFun
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import java.nio.channels.Channels
import java.util.Timer
import java.util.TimerTask


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UpdateScreen(
    viewModel: ChatAppViewModel,
    navController: NavController
) {

    val sortedStatusList by viewModel.sortedStatusList.collectAsState()
    val sortedChannelList by viewModel.sortedChannelList.collectAsState()
    val isSearchBarActive by viewModel.isUpdateScreenSearchBarActive.collectAsState()

    val searchinputtext = remember { mutableStateOf("") }
    val isFocused by viewModel.isUpdateScreenSearchBarFocused.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val listOfStatus = loadStatuses()
    val filteredStatus = listOfStatus.filter { contact ->
        contact.username.contains(searchinputtext.value, ignoreCase = true)
    }
    val listOfChannels = loadChannels()
    val filteredChannels = listOfChannels.filter { contact ->
        contact.channelname.contains(searchinputtext.value, ignoreCase = true)
    }


    if (searchinputtext.value.isNotEmpty() && isSearchBarActive) {
        // updating status data........
        viewModel.updateSortedStatusList(filteredStatus)
    } else {
        viewModel.updateSortedStatusList(loadStatuses())
    }

    if (searchinputtext.value.isNotEmpty() && isSearchBarActive) {
        // updating channel data.....
        viewModel.updateSortedChannelList(filteredChannels)
    } else {
        viewModel.updateSortedChannelList(loadChannels())
    }


//    BackHandler {
//        viewModel.changeUpdateScreenSearchBarActiveState(false)
//        navigateIfNotFast {
//            navController.navigate("home_screen")
//        }
//
//    }
    val showProfilePicDialogues = remember {
        mutableStateOf(false)
    }


    MyChattingAppTheme {

        LazyColumn(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight()
        ) {
            if (isSearchBarActive) {
                stickyHeader {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
                        shape = RectangleShape,

                        ) {

                        Stscrscbar(
                            searchinputtext = searchinputtext,
                            focusRequester = focusRequester,
                            viewModel = viewModel,
                            isFocused = isFocused,
                            focusManager = focusManager
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }


                    LaunchedEffect(Unit) {
                        focusRequester.requestFocus()
                    }

                }
            }

            // Before Channel Contents........
            item {
                // Status Contents.............
                LazyRow(horizontalArrangement = Arrangement.SpaceEvenly) {
                    items(
                        sortedStatusList
                    ) { i ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                i.statusicon()
                                Text(i.username)

                            }
                        }
                    }


                }

                HorizontalDivider(thickness = 2.dp, color = Color.Black)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(4.dp)
                ) {
                    Box {
                        Text("Channels", fontSize = 20.sp)
                    }
                    Box(modifier = Modifier.clickable { }) {
                        Text("Explore", fontSize = 20.sp)
                    }
                }


            }

            // Rendering Channels.........
            items(sortedChannelList) { channel ->
                ChatRow(
                    contactName = channel.channelname,
                    recentMessage = channel.lastMessage,
                    messageSentTime = channel.timeStamps,
//                    icon = channel.channelicon
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (sortedStatusList.isEmpty() && sortedChannelList.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No results found", textAlign = TextAlign.Center)
                    }
                }
            }
            if (sortedChannelList.size > 5) {

                item {
                    Spacer(modifier=Modifier.height(20.dp))
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
    }

}


@Composable
fun UpdateScreenTopAppBar(
    viewModel: ChatAppViewModel
) {

    val isSearchBarActive by viewModel.isUpdateScreenSearchBarActive.collectAsState()

    if (!isSearchBarActive) {
        TopBarFun(
            firstIcon = {
            },
            text = "Updates",
            color = Color.Transparent,
            body = {

                IconButton(
                    onClick = {}

                )
                {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground


                    )
                }
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.clickable(onClick = {
                        viewModel.changeUpdateScreenSearchBarActiveState(true)


                    })

                )
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

            }

        )
    }

}

@Composable
fun Stscrscbar(
    modifier: Modifier = Modifier,
    searchinputtext: MutableState<String>,
    focusRequester: FocusRequester,
    viewModel: ChatAppViewModel,
    isFocused: Boolean,
    focusManager: FocusManager,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            keyboardController?.hide()
        }),
        value = searchinputtext.value,
        onValueChange = {
            searchinputtext.value = it
        },
        placeholder = {
            Text(text = "Search")
        },
        label = {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FaIcon(
                    faIcon = FaIcons.GgCircle,
                    tint = MaterialTheme.colorScheme.primary,
                    size = 15.dp
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text("Search to find desired item")
                Spacer(modifier = Modifier.height(20.dp))
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged {
                // Storing the active state of the searchbar in the variable isFocused.....
                viewModel.changeUpdateScreenSearchBarFocusedState(it.isFocused)
            },
        leadingIcon = {
            if (isFocused) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "search",
                    modifier = Modifier.clickable {
                        searchinputtext.value = ""
                        focusManager.clearFocus()
                        viewModel.changeUpdateScreenSearchBarActiveState(false)

                    }
                )
            }
        },
        trailingIcon = {
            if (isFocused) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "search",
                    modifier = Modifier.clickable {
                        searchinputtext.value = ""
                    })
            }
        },
        supportingText = {
            if (isFocused) {
                if (searchinputtext.value == "") {

                    Text(text = "Search for contact or channel name")
                } else {
                    Text(text = "Matched contacts or channel for \"${searchinputtext.value}\"")
                }
            }
        },
        shape = RoundedCornerShape(50.dp),
    )

}




