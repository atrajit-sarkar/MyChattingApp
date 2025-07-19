package com.example.mychattingapp.LocaldbLogics.DAO.Entities

import androidx.compose.runtime.Composable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],  // Column in User entity
            childColumns = ["chatId"],  // Column in Message entity
            onDelete = ForeignKey.CASCADE  // Deletes messages if the chat is deleted
        )
    ],
    indices = [Index(value = ["chatId"])]  // Index for faster queries on chatId
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val chatId: Int,  // Foreign key linking to Chat entity
    val messageId: String = "",
    var sender: String,
    var receiver: String = "",
    var text: String,
    var timestamp: String,
    var reaction: String,
    var icons: String = "",
    var password: String = "",
    var viewOnce: String = "",
    var editIcon: String = "",
    var deletedForMe: String = "",
    var deletedForEveryone: String = "",
    var fileType: String = "",
    var fileUri: String = ""
)
