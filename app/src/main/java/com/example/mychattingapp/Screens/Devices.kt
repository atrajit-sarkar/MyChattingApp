package com.example.mychattingapp.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mychattingapp.ui.theme.MyChattingAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun DevicesScreenUI() {
    MyChattingAppTheme {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Devices",
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = {
                        IconButton(onClick = { /* Handle back navigation */ }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )

            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                items(5) { index ->
                    DeviceItemUI(
                        deviceName = if (index == 0) "Your Device" else "Device $index",
                        os = "OS: Android ${12 - index}",
                        lastActive = if (index == 0) "Just now" else "$index hours ago",
                        isCurrentDevice = index == 0
                    )

                }


                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    LogoutAllButtonUI()
                }
            }
        }
    }

}

@Composable
fun DeviceItemUI(
    deviceName: String,
    os: String,
    lastActive: String,
    isCurrentDevice: Boolean
) {
    MyChattingAppTheme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = deviceName, style = MaterialTheme.typography.headlineLarge)
                Text(
                    text = os,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "Last Active: $lastActive",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )


                if (isCurrentDevice) {
                    Text(
                        text = "This is your current device",
                        color = Color.Green,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else {
                    Button(
                        onClick = { /* Handle logout for this device */ },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Logout")
                    }
                }
            }
        }
    }

}

@Composable
fun LogoutAllButtonUI() {
    MyChattingAppTheme {
        Button(
            onClick = { /* Handle logout all */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Logout from All Devices")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DevicesScreenUIPreview() {
    DevicesScreenUI()
}
