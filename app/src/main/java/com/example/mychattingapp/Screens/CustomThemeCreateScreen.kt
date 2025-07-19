package com.example.mychattingapp.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.CustomChatTheme
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.NavHost.navigateIfNotFast
import com.example.mychattingapp.ui.theme.MyChattingAppTheme
import com.example.mychattingapp.widgets.ChatThemeWidgets.ColorWheelPicker

//@Preview
@Composable
fun CustomThemeCreateScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatAppViewModel,
    navController: NavController
) {
    val ownMessageColor = remember { mutableStateOf("") }
    val ownMessageColorHues = remember { mutableStateOf(0f) }
    val ownMessageColorOffSet = remember { mutableStateOf<Offset?>(null) }

    val notOwnMessageColor = remember { mutableStateOf("") }
    val notOwnMessageColorHues = remember { mutableStateOf(0f) }
    val notOwnMessageColorOffSet = remember { mutableStateOf<Offset?>(null) }

    val ownBorderColor = remember { mutableStateOf("") }
    val ownBorderColorHues = remember { mutableStateOf(0f) }
    val ownBorderColorOffSet = remember { mutableStateOf<Offset?>(null) }

    val notOwnBorderColor = remember { mutableStateOf("") }
    val notOwnBorderColorHues = remember { mutableStateOf(0f) }
    val notOwnBorderColorOffSet = remember { mutableStateOf<Offset?>(null) }

    val lockColor = remember { mutableStateOf("") }
    val lockColorHues = remember { mutableStateOf(0f) }
    val lockColorOffSet = remember { mutableStateOf<Offset?>(null) }

    val viewOnceColor = remember { mutableStateOf("") }
    val viewOnceColorHues = remember { mutableStateOf(0f) }
    val viewOnceColorOffSet = remember { mutableStateOf<Offset?>(null) }

    val lockOpenColor = remember { mutableStateOf("") }
    val lockOpenColorHues = remember { mutableStateOf(0f) }
    val lockOpenColorOffSet = remember { mutableStateOf<Offset?>(null) }

    val openedColor = remember { mutableStateOf("") }
    val openedColorHues = remember { mutableStateOf(0f) }
    val openedColorOffSet = remember { mutableStateOf<Offset?>(null) }

    val themeName = remember { mutableStateOf("") }

    MyChattingAppTheme {
        Scaffold { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    TextField(
                        value = themeName.value,
                        onValueChange = {
                            themeName.value = it
                        },
                        label = {
                            Text("Enter ChatThemeName")
                        },
                        supportingText = {
                            Text("Must Select the colors Below")
                        }


                    )
                }
                item {

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 3.dp)
                }
                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text("ownMessageColor")
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedVisibility(ownMessageColor.value != "") {
                                // Selected Color Preview
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(Color(ownMessageColor.value.toLong()), shape = RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                )
                            }
                            ColorWheelPicker(
                                onColorSelected = {
                                    ownMessageColor.value = it.toString()
                                },
                                hue = ownMessageColorHues,
                                offSet = ownMessageColorOffSet
                            )
                        }


                    }
                }
                item {

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 3.dp)
                }
                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text("Not ownMessageColor")
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedVisibility(notOwnMessageColor.value != "") {
                                // Selected Color Preview
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(Color(notOwnMessageColor.value.toLong()), shape = RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                )
                            }
                            ColorWheelPicker(
                                onColorSelected = {
                                    notOwnMessageColor.value = it.toString()
                                },
                                hue = notOwnMessageColorHues,
                                offSet = notOwnMessageColorOffSet
                            )
                        }


                    }
                }
                item {

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 3.dp)
                }
                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text("ownBorderColor")
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedVisibility(ownBorderColor.value != "") {
                                // Selected Color Preview
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(Color(ownBorderColor.value.toLong()), shape = RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                )
                            }
                            ColorWheelPicker(
                                onColorSelected = {
                                    ownBorderColor.value = it.toString()
                                },
                                hue = ownBorderColorHues,
                                offSet = ownBorderColorOffSet
                            )
                        }


                    }
                }
                item {

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 3.dp)
                }
                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text("NotownBorderColor")
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedVisibility(notOwnBorderColor.value != "") {
                                // Selected Color Preview
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(Color(notOwnBorderColor.value.toLong()), shape = RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                )
                            }
                            ColorWheelPicker(
                                onColorSelected = {
                                    notOwnBorderColor.value = it.toString()
                                },
                                hue = notOwnBorderColorHues,
                                offSet = notOwnBorderColorOffSet
                            )
                        }

                    }
                }
                item {

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 3.dp)
                }
                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text("Lock Color")
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedVisibility(lockColor.value != "") {
                                // Selected Color Preview
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(Color(lockColor.value.toLong()), shape = RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                )
                            }
                            ColorWheelPicker(
                                onColorSelected = {
                                    lockColor.value = it.toString()
                                },
                                hue = lockColorHues,
                                offSet = lockColorOffSet
                            )
                        }


                    }
                }
                item {

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 3.dp)
                }
                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text("View Once Color")
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedVisibility(viewOnceColor.value != "") {
                                // Selected Color Preview
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(Color(viewOnceColor.value.toLong()), shape = RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                )
                            }
                            ColorWheelPicker(
                                onColorSelected = {
                                    viewOnceColor.value = it.toString()
                                },
                                hue = viewOnceColorHues,
                                offSet = viewOnceColorOffSet
                            )
                        }


                    }
                }
                item {

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 3.dp)
                }
                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text("Lock Open Color")
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedVisibility(lockOpenColor.value != "") {
                                // Selected Color Preview
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(Color(lockOpenColor.value.toLong()), shape = RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                )
                            }
                            ColorWheelPicker(
                                onColorSelected = {
                                    lockOpenColor.value = it.toString()
                                },
                                hue = lockOpenColorHues,
                                offSet = lockOpenColorOffSet
                            )
                        }


                    }
                }
                item {

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 3.dp)
                }
                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text("Opened Color")
                            Spacer(modifier = Modifier.height(10.dp))
                            AnimatedVisibility(openedColor.value != "") {
                                // Selected Color Preview
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(Color(openedColor.value.toLong()), shape = RoundedCornerShape(8.dp))
                                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                )
                            }
                            ColorWheelPicker(
                                onColorSelected = {
                                    openedColor.value = it.toString()
                                },
                                hue = openedColorHues,
                                offSet = openedColorOffSet
                            )
                        }


                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(thickness = 3.dp)
                    Button(
                        enabled = ownMessageColor.value != "" && notOwnMessageColor.value != "" && ownBorderColor.value != "" && notOwnBorderColor.value != "" && lockColor.value != "" && viewOnceColor.value != "" && lockOpenColor.value != "" && openedColor.value != "" && themeName.value != "",
                        onClick = {
                            viewModel.insertCustomChatTheme(
                                customChatTheme = CustomChatTheme(
                                    ownMessageColor = ownMessageColor.value,
                                    notOwnMessageColor = notOwnMessageColor.value,
                                    ownBorderColor = ownBorderColor.value,
                                    notOwnBorderColor = notOwnBorderColor.value,
                                    lockColor = lockColor.value,
                                    viewOnceColor = viewOnceColor.value,
                                    lockOpenColor = lockOpenColor.value,
                                    openedColor = openedColor.value,
                                    themeName = themeName.value
                                )
                            )
                            navigateIfNotFast() {
                                navController.popBackStack()
                            }

                        }
                    ) {
                        Text("Save the theme")
                    }
                }


            }
        }
    }

}