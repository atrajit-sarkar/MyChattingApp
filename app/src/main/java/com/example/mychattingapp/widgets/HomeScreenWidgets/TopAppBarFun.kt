package com.example.mychattingapp.widgets.HomeScreenWidgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBarFun(
    firstIcon: @Composable () -> Unit = {},
    text: String = "Chatting App",
    fontsize: TextUnit=26.sp,
    caption: String = null.toString(),
    size: TextUnit? = null,
    body: @Composable () -> Unit = {},
    color: Color = Color.Magenta,
    navController: NavController = rememberNavController(),

    ) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.fillMaxHeight()
                ) {
                    firstIcon()
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {

                    Text(
                        text = text,
                        fontSize = fontsize
                    )
                        if (size != null) {
                            Text(
                                text= caption,
                                fontSize = size
                            )
                        }
                    }
                }
                Row (horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(5.dp)){
                    body()
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(color)
    )
}

