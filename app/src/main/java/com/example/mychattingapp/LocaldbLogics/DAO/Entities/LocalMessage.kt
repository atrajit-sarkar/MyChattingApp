package com.example.mychattingapp.LocaldbLogics.DAO.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "local_messages")
data class LocalMessage(
    @PrimaryKey
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
    var id: String = "",
    var fileType: String = "",
    var fileUri: String = ""
)
