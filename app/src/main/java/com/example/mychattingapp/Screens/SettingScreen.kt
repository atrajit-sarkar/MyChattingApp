package com.example.mychattingapp.Screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.benchmark.perfetto.Row
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.mychattingapp.FireBaseLogics.Utils.generateProfilePicUUID
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.NavHost.navigateIfNotFast
import com.example.mychattingapp.ProfilePicUploadLogics.uploadToGitHub
import com.example.mychattingapp.ui.theme.MyChattingAppTheme
import com.example.mychattingapp.widgets.HomeScreenWidgets.TopBarFun
import com.example.mychattingapp.widgets.SettingsScreenWidgets.ProfilePicPickerBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@OptIn(DelicateCoroutinesApi::class)
@Suppress(names = ["DEPRECATION"])
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: ChatAppViewModel
) {
    val fontSize = remember { mutableFloatStateOf(0.8f) }
    val currentUser by viewModel.currentUser.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val currentuser = FirebaseAuth.getInstance().currentUser
    val uid = currentuser?.uid
    val showProfilePicDialogues = remember { mutableStateOf(false) }
    val targetUser = viewModel.filterUser(uid.toString()).collectAsState()
    val profilePicUri = remember { mutableStateOf("") }
    val contentResolver = context.contentResolver
    var selectedFile = remember { mutableStateOf<File?>(null) }
    val toastMessage = viewModel.toastMessage.observeAsState()
    val uploadingProfilePhoto = viewModel.uploadingProfilePhoto.collectAsState()
    val auth = Firebase.auth
    val specialUri = remember { mutableStateOf("") }

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
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    text = "Settings",
                    color = MaterialTheme.colorScheme.primaryContainer,
                    body = {
                        IconButton(onClick = {
                            navigateIfNotFast {
                                navController.navigate("search_screen")
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                )
            },
        ) { innerPadding ->
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(all = 10.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) {
                                navigateIfNotFast {
                                    navController.navigate("profile_screen")
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Card(
                            shape = RoundedCornerShape(90.dp),
                            modifier = Modifier.size(80.dp),
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                            onClick = {}
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (targetUser.value.isNotEmpty()) {
                                    if (targetUser.value[0].profilePicUri == "") {
                                        FaIcon(
                                            faIcon = FaIcons.UserCircle,
                                            size = 80.dp,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.clickable(
                                                onClick = {
                                                    showProfilePicDialogues.value = true
                                                }
                                            )
                                        )
                                    } else {
                                        Image(
                                            painter = rememberAsyncImagePainter(
                                                model = if (uploadingProfilePhoto.value) profilePicUri.value else "https://raw.githubusercontent.com/gongobongofounder/MyChattingAppProfilePics/main/${targetUser.value[0].profilePicUri}"
                                            ),
                                            contentDescription = "WhatsApp DP",
                                            modifier = Modifier
                                                .size(90.dp)
                                                .clip(CircleShape) // Clip to circular shape
                                                .border(
                                                    2.dp,
                                                    Color.LightGray,
                                                    CircleShape
                                                ) // Add a green border
                                                .clickable(
                                                    onClick = {
                                                        showProfilePicDialogues.value = true
                                                    }
                                                ),
                                            contentScale = ContentScale.Crop // Automatically crop to fit the circular shape
                                        )
                                    }
                                }
                                if (uploadingProfilePhoto.value) {
                                    CircularProgressIndicator(color = Color(0xFF4D9661))
                                }

                            }
                        }
                        if (showProfilePicDialogues.value) {
                            ProfilePicPickerBottomSheet(
                                showBottomSheet = showProfilePicDialogues,
                                onProfilePicSelected = { uri ->
                                    viewModel.changeUploadingProfilePhotoState(true)
                                    profilePicUri.value = uri.toString()
                                    specialUri.value="${auth.currentUser?.email.toString()}${generateProfilePicUUID()}"

                                    // Open and copy the file to cache asynchronously
                                    GlobalScope.launch(Dispatchers.IO) {
                                        val selectedFile = uri.let { uri ->
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
                                                token = "",
                                                owner = "gongobongofounder",
                                                repo = "MyChattingAppProfilePics",
                                                filePath = "${specialUri.value}.jpg",
                                                file = file,
                                                specialUri = specialUri.value,
                                                viewModel = viewModel
                                            )
                                        }
                                    }
                                },
                                viewModel = viewModel

                            )
                        }

                        if (!uploadingProfilePhoto.value && toastMessage.value != "") {
                            toastMessage.value?.let { message ->
                                LaunchedEffect(message) {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    viewModel.setToastMessage("") // Reset toast message
                                }
                            }
                            viewModel.setToastMessage("")
                        }
                        Spacer(modifier = Modifier.width(10.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .animateContentSize()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = currentUser,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = (10 + fontSize.floatValue * 25).toInt().sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Hey There! I Am Using ChatApp",
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontSize = (10 + fontSize.floatValue * 5).toInt().sp
                                    )
                                }

                                IconButton(onClick = {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, " $uid")
                                        putExtra(Intent.EXTRA_TITLE, "$currentuser")
                                        type = "text/plain"
                                    }
                                    val chooser = Intent.createChooser(shareIntent, "Share via")
                                    context.startActivity(chooser)
                                }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
                    Column {
                        SettingsRow(
                            label = "Sign Out",
                            caption = "Log out of your account",
                            iconSolid = FaIcons.Key,
                            fontSize = (10 + fontSize.floatValue * 25).toInt().sp,
                            cardOnClick = { showDialog.value = true }
                        )
                        SignOutDialog(
                            showDialog = showDialog.value,
                            onConfirm = {
                                val updateSignOut = mapOf(
                                    "activeStatus" to "Signed Out 😴"
                                )
                                viewModel.updateUserItem(
                                    viewModel.currentUserId.value,
                                    updateSignOut
                                )
                                FirebaseAuth.getInstance().signOut()
                                navigateIfNotFast {
                                    navController.navigate("login_screen") {
                                        popUpTo("home_screen") { inclusive = true }
                                    }
                                }
                                showDialog.value = false
                            },
                            onDismiss = { showDialog.value = false }
                        )

                        SettingsRow(
                            label = "Instructions",
                            caption = "Optimize message delivery",
                            iconSolid = FaIcons.Info,
                            fontSize = (10 + fontSize.floatValue * 25).toInt().sp,
                            cardOnClick = {
                                navigateIfNotFast { navController.navigate("setup_time_screen") }
                            }
                        )

                        // Add other settings rows similarly
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsRow(
    label: String,
    caption: String = "",
    size: TextUnit = 15.sp,
    iconSolid: FaIconType.SolidIcon? = null,
    iconBrand: FaIconType.BrandIcon? = null,
    iconRegular: FaIconType.RegularIcon? = null,
    cardOnClick: () -> Unit = {},
    iconOnClick: () -> Unit = {},
    fontSize: TextUnit = 33.sp,
    settingsrowpadding: Dp = 10.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),

        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(settingsrowpadding),
            colors = CardDefaults.cardColors(Color.Transparent),
            onClick = {
                cardOnClick()
            }
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        iconOnClick()
                    }

                ) {
                    if (iconSolid != null) {

                        FaIcon(
                            faIcon = iconSolid,
                            tint = Color.White
                        )
                    } else if (iconBrand != null) {
                        FaIcon(
                            faIcon = iconBrand,
                            tint = Color.White
                        )
                    } else if (iconRegular != null) {
                        FaIcon(
                            faIcon = iconRegular,
                            tint = Color.White
                        )
                    }
                }


                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        text = label,
                        color = Color.Green,
                        fontSize = fontSize,
                        textAlign = TextAlign.Center
                    )
                    if (caption != "") {

                        Text(
                            text = caption,
                            color = Color.Green,
                            fontSize = size,
                            textAlign = TextAlign.Center
                        )
                    }
                }


            }


        }
    }
}


@Composable
fun SignOutDialog(
    showDialog: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(
                    onClick = { onConfirm() }
                ) {
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismiss() }
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            },
            title = {
                Text(
                    text = "Sign Out",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to sign out? You will need to log in again to access your account.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Start
                    )
                )
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun ProfilePictureSetDialogue(
    modifier: Modifier = Modifier,
    showProfilePicDialogues: MutableState<Boolean>,
    viewModel: ChatAppViewModel
) {
    val profilePicUrl = remember { mutableStateOf("") }
    if (showProfilePicDialogues.value) {
        Dialog(onDismissRequest = { showProfilePicDialogues.value = false }) {
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
                        text = "Enter Profile pic name",
                        style = MaterialTheme.typography.titleMedium
                    )
                    // Password TextField
                    OutlinedTextField(
                        value = profilePicUrl.value,
                        onValueChange = { profilePicUrl.value = it },
                        label = { Text("Profile Pic Uri") },
                        singleLine = true,
                        placeholder = {
                            Text("Enter the profile Pic uri")
                        },
                        supportingText = {
                            Text("Example: \"githubUsername\\RepoName\\main\\gongoBongo.jpg\"")
                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            showProfilePicDialogues.value = false
                            profilePicUrl.value = ""
                        }) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = {
                                showProfilePicDialogues.value = false
                                val profilePicUpdate = mapOf(
                                    "profilePicUri" to profilePicUrl.value
                                )
                                viewModel.updateUserItem(
                                    viewModel.currentUserId.value,
                                    profilePicUpdate
                                )


                            },
                            enabled = profilePicUrl.value.isNotEmpty()
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }

}

fun getFileName(context: Context, uri: Uri): String? {
    var fileName: String? = null
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst() && nameIndex != -1) {
            fileName = it.getString(nameIndex)
        }
    }
    return fileName
}
