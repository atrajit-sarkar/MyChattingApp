package com.example.mychattingapp.LocaldbLogics.Repositories

import com.example.mychattingapp.LocaldbLogics.DAO.Entities.UserDocId
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.UserDocIdDao
import javax.inject.Inject

class UserDocIdRepo @Inject constructor(private val userDocIdDao: UserDocIdDao) {
    suspend fun insertUserDocId(userDocId: UserDocId) {
        userDocIdDao.insertUserDocId(userDocId)
    }

    suspend fun deleteUserDocId(userdocid: String) {
        userDocIdDao.deleteUserDocId(userdocid)
    }

    fun getAllUserDocIds() = userDocIdDao.getAllUserDocIds()

}