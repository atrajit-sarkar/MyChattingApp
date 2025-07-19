package com.example.mychattingapp.Screens

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mychattingapp.R
import com.example.mychattingapp.ui.theme.MyChattingAppTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//@Preview(showBackground = true)
@Composable
fun ChatAppProfileSection(
    navController: NavController
) {
    val auth = Firebase.auth
    val context = LocalContext.current

    MyChattingAppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(4.dp),
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = auth.currentUser?.email.toString(),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = {
                        // Add functionality toCopy Text to clipBoard
                        val uid = auth.currentUser?.uid.orEmpty()
                        val clipboardManager =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("UID", uid)
                        clipboardManager.setPrimaryClip(clip)

                        Toast.makeText(
                            context,
                            "UID copied to clipboard!",
                            Toast.LENGTH_SHORT
                        ).show()
                    },

                ) {
                    FaIcon(
                        faIcon = FaIcons.Copy,
                        tint = Color.Gray,
                    )
                    Text(
                        text = auth.currentUser?.uid.toString(),
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(10.dp),
                        fontSize = 18.sp
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Hey there! I'm using Chat App.",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )


            }
//            Column(modifier = Modifier.fillMaxWidth()) {
//                Box(modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(10.dp)) {
//                    Text(text = "Profile")
//
//                }
//            }

        }

    }


}




