package com.example.mychattingapp.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mychattingapp.FireBaseLogics.collectUsersFromFirestore
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.NavHost.navigateIfNotFast
import com.example.mychattingapp.ui.theme.MyChattingAppTheme
import com.example.mychattingapp.widgets.HomeScreenWidgets.ChatRow
import com.example.mychattingapp.widgets.HomeScreenWidgets.TopBarFun
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.guru.fontawesomecomposelib.FaIcons

@Composable
fun Contacts(
    navController: NavController,
    viewModel: ChatAppViewModel
) {
    val allContacts by viewModel.userList.collectAsState(emptyList())
    val contactLoading by viewModel.contactLoading.collectAsState()
    val auth: FirebaseAuth = Firebase.auth
    val currentUserUid = auth.currentUser?.uid ?: ""
    val showProfilePicDialogues = remember {
        mutableStateOf(false)
    }

    MyChattingAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBarFun(
                    firstIcon = {
                        IconButton(onClick = {
                            navigateIfNotFast {
                                navController.popBackStack()
                            }

                        }) {

                            Icon(
                                imageVector = Icons.Default.ArrowBack, contentDescription = null


                            )
                        }
                    },
                    text = "Select contact",
                    caption = "All contacts",
                    size = 15.sp,
                    fontsize = 20.sp,
                    color = Color(0xFF2d944c),
                    body = {
                        IconButton(
                            onClick = {}

                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White,
//                                modifier = Modifier.fillMaxSize()
                            )

                        }
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = Color.White,
//                                modifier = Modifier.fillMaxSize()
                        )
                    }


                )


            },


//            bottomBar = {},
        ) { innerPadding ->


            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                item {
                    SettingsRow(
                        label = "New group",
                        iconSolid = FaIcons.UserFriends,
                        fontSize = 25.sp


                    )
                    SettingsRow(
                        label = "New contact",
                        iconSolid = FaIcons.File,
                        fontSize = 25.sp


                    )
                    SettingsRow(
                        label = "New community",
                        iconSolid = FaIcons.LayerGroup,
                        fontSize = 25.sp


                    )
                    HorizontalDivider(thickness = 1.dp, color = Color.White)
                    Row(Modifier.padding(horizontal = 10.dp, vertical = 1.dp)) {
                        Text("Users on FireBaseFireStore", color = Color.Green)
                    }

                }
                if (contactLoading) {
                    item {
                        // Circular Loading Indicator desinged professionally
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            CircularProgressIndicator()
                        }
                    }
                } else {

                    items(allContacts) { user ->
                        ChatRow(
                            contactName = if (user.uid == currentUserUid) "(You) ${user.userName}" else user.userName,
                            recentMessage = user.recentMessage,
                            messageSentTime = user.messageSentTime,
                            onClick = {
                                navigateIfNotFast {
                                    navController.navigate("chat_screen/${user.uid}")
                                }
                                viewModel.addUser(user)

                            },
                            profilePicUri = user.profilePicUri,
                        )

                    }
                }
            }


        }
    }
}

//@Preview
//@Composable
//fun PreviewContacts() {
//    Contacts(
//        navController = rememberNavController()
//    )
//}
