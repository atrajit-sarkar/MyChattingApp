package com.example.mychattingapp.widgets.ChatScreenWidgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.User
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.NavHost.navigateIfNotFast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ChatScreenTopBar(
    navController: NavController,
    contactName: String,
    lastSeen: String,
    viewModel: ChatAppViewModel,
    user: User
) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val subexpanded = remember {
        mutableStateOf(false)
    }
    val clearChatDialog = remember {
        mutableStateOf(false)
    }
//    val localUser by viewModel.us
    val auth = Firebase.auth
    TopAppBar(
        navigationIcon = {
            // Back button
            IconButton(onClick = {
                navigateIfNotFast() {
                    navController.popBackStack()

                }
                viewModel.changeUid("")
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Contact Profile Picture
//                Icon(
//                    imageVector = Icons.Default.AccountCircle,
//                    contentDescription = "Profile Picture",
//                    modifier = Modifier.size(40.dp),
//                    tint = MaterialTheme.colorScheme.onBackground
//                )
                Image(
                    painter = rememberAsyncImagePainter(
                        model = "https://raw.githubusercontent.com/gongobongofounder/MyChattingAppProfilePics/main/${user.profilePicUri}"
                    ),
                    contentDescription = "WhatsApp DP",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape) // Clip to circular shape
                        .border(2.dp, Color.LightGray, CircleShape) // Add a green border
                    ,
                    contentScale = ContentScale.Crop // Automatically crop to fit the circular shape
                )
                Spacer(modifier = Modifier.width(8.dp))

                // Contact Name and Last Seen
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f) // Take remaining space for text
                ) {
                    Text(
                        text = contactName,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis // Truncate with ellipsis if too long
                    )
                    if (lastSeen != "Typing....") {
                        Text(
                            text = lastSeen,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1
                        )
                    } else {
                        Text(
                            text = "Online",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1
                        )
                    }
                }

                // Action Icons (Video Call, Call, More Options)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {}) {
                        FaIcon(
                            faIcon = FaIcons.Video,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    IconButton(onClick = {
                        // DropDownMenu for Controlling Chats......
                        expanded.value = !expanded.value

                    }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More Options",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        DropDownMenuChatScreen(expanded, navController, subexpanded = subexpanded)
                        SubDropDownMenuChatScreen(subexpanded, clearChatDialog = clearChatDialog)
                        if (clearChatDialog.value) {

                            ClearChatDialogue(showDialog = clearChatDialog, viewModel, user)
                        }
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),
        modifier = Modifier.fillMaxWidth()
    )

}

