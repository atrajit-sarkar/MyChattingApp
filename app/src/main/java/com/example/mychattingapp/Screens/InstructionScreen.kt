package com.example.mychattingapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mychattingapp.R
import com.example.mychattingapp.ui.theme.MyChattingAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupTimeInstructionsScreen() {

    MyChattingAppTheme {
        androidx.compose.material3.Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Setup Time Automatically") },
                    colors = TopAppBarDefaults.topAppBarColors(Color(0xFF6200EE)),

                    )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = "Follow these steps to set your mobile device's time automatically using the network:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    InstructionStep(
                        stepNumber = 1,
                        stepDescription = "Go to your device's Settings.",
                        imageRes = R.mipmap.gotosettings_foreground
                    )
                    InstructionStep(
                        stepNumber = 2,
                        stepDescription = "Navigate to Date & Time settings.",
                        imageRes = R.mipmap.navigatetodateandtime_foreground
                    )
                    InstructionStep(
                        stepNumber = 3,
                        stepDescription = "Enable the option to set time automatically.",
                        imageRes = R.mipmap.enableautomatic_foreground
                    )
                }

            }
        }


    }
}

@Composable
fun InstructionStep(stepNumber: Int, stepDescription: String, imageRes: Int?) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "$stepNumber.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stepDescription,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Illustration for step $stepNumber",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(top = 8.dp)
            )
        }
    }
}


