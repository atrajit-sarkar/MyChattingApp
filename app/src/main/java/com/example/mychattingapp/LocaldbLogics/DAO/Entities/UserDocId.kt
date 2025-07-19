package com.example.mychattingapp.LocaldbLogics.DAO.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_doc_id")
data class UserDocId(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userdocid: String
)
