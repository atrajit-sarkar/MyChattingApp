package com.example.mychattingapp.ChatsData

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons


data class UsersStatusData(
    var username: String,
    var statusicon: @Composable () -> Unit
)

fun loadStatuses(): List<UsersStatusData> {
    return listOf<UsersStatusData>(
        UsersStatusData(username = "Atrajit",
            statusicon = {
                FaIcon(
                    faIcon = FaIcons.UserCircle,
                    size = 80.dp,
                    tint = MaterialTheme.colorScheme.onBackground

                )

            }),
        UsersStatusData(username = "soutam",
            statusicon = {
                FaIcon(
                    faIcon = FaIcons.UserCircle,
                    size = 80.dp,
                    tint = MaterialTheme.colorScheme.onBackground

                )

            }),
        UsersStatusData(username = "tukun",
            statusicon = {
                FaIcon(
                    faIcon = FaIcons.UserCircle,
                    size = 80.dp,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }),
        UsersStatusData(username = "parna",
            statusicon = {
                FaIcon(
                    faIcon = FaIcons.UserCircle,
                    size = 80.dp,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }),
        UsersStatusData(username = "rose vora bati",
            statusicon = {
                FaIcon(
                    faIcon = FaIcons.UserCircle,
                    size = 80.dp,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }),
        UsersStatusData(username = "dudu",
            statusicon = {
                FaIcon(
                    faIcon = FaIcons.UserCircle,
                    size = 80.dp,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }),
        UsersStatusData(username = "chodu",
            statusicon = {
                FaIcon(
                    faIcon = FaIcons.UserCircle,
                    size = 80.dp,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }),



    )

}
