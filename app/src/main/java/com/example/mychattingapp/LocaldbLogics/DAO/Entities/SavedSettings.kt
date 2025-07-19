package com.example.mychattingapp.LocaldbLogics.DAO.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_settings")
data class SavedSettings(
    @PrimaryKey val id: Int = 1,
    val language: String = "",
    val chatTheme: String = "",
    val wallpaper: String = "",
    val customChatThemeId: Int = 0
)
