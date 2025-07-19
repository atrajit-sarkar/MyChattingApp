package com.example.mychattingapp.LocaldbLogics.DAO.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_chat_theme")
data class CustomChatTheme(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var themeName: String,
    var ownMessageColor: String,
    var notOwnMessageColor: String,
    var ownBorderColor: String,
    var notOwnBorderColor: String,
    var lockColor: String,
    var viewOnceColor: String,
    var lockOpenColor: String,
    var openedColor: String,
)
