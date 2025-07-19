package com.example.mychattingapp.widgets.ChatScreenWidgets

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MessegeSelectDropDownMenu(
    expanded: MutableState<Boolean>,
    viewModel: ChatAppViewModel
) {
    val selectedMessages by viewModel.selectedMessages.collectAsState()

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        shadowElevation = 10.dp,
        tonalElevation = 10.dp,
        offset = DpOffset(x = 10.dp, y = 10.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.width(180.dp)
    ) {
        DropdownMenuItem(
            onClick = {},
            text = {
                Text(
                    "Star",
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.Star,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        )
        DropdownMenuItem(
            onClick = {},
            text = {
                Text(
                    "Copy",
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.Copy,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        )
        if (selectedMessages.size == 1 && selectedMessages[0].sender == viewModel.currentUserId.value) {
                DropdownMenuItem(
                    onClick = {
                        expanded.value = false

                        viewModel.isEditingInitiated(true)


                    },
                    text = {
                        Text(
                            "Edit",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    leadingIcon = {
                        FaIcon(
                            faIcon = FaIcons.PencilRuler,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )

        }
        DropdownMenuItem(
            onClick = {},
            text = {
                Text(
                    "Pin",
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            leadingIcon = {
                FaIcon(
                    faIcon = FaIcons.MapPin,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        )

    }
}