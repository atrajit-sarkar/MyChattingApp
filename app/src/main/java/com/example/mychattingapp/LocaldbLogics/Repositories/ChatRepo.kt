package com.example.mychattingapp.LocaldbLogics.Repositories

import com.example.mychattingapp.LocaldbLogics.DAO.Entities.LocalMessage
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.Message
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.MessageDao
import javax.inject.Inject

class ChatRepository @Inject constructor(private val messageDao: MessageDao) {
    fun getAllMessages() = messageDao.getAllMessages()
    suspend fun insertMessage(message: LocalMessage) = messageDao.insertMessage(message)
    suspend fun deleteMessage(message: LocalMessage)=messageDao.deleteMessages(message)
    suspend fun deleteAllMessage(uid: String,currentUserId: String)=messageDao.deleteAllMessages(uid,currentUserId)
    suspend fun updateMessage(message: LocalMessage)=messageDao.updateMessage(message)
    fun getMessageById(uid: String,currentUserId: String) = messageDao.getMessageById(uid,currentUserId)
}