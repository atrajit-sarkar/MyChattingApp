package com.example.mychattingapp.LocaldbLogics.DAO.RoomDbConnection

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.CustomChatTheme
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.LocalMessage
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.Message
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.SavedSettings
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.User
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.UserDocId
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.CustomChatThemeDao
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.MessageDao
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.SavedSettingsDao
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.UserDao
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.UserDocIdDao

@Database(entities = [LocalMessage::class,User::class,UserDocId::class,SavedSettings::class, CustomChatTheme::class, Message::class], version = 25, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun userDao(): UserDao
    abstract fun userDocIdDao(): UserDocIdDao
    abstract fun savedSettingsDao(): SavedSettingsDao
    abstract fun customChatThemeDao(): CustomChatThemeDao
}