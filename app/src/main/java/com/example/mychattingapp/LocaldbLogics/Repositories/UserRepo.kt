package com.example.mychattingapp.LocaldbLogics.Repositories

import com.example.mychattingapp.LocaldbLogics.DAO.Entities.Message
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.User
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.MessageDao
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {
    fun getAllUsers() = userDao.getAllUsers()
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun deleteUser(user: User)=userDao.deleteUser(user)
    suspend fun deleteAllUser()=userDao.deleteAllUsers()
    suspend fun updateUser(user: User)=userDao.updateUser(user)
    fun getUserById(userId:Int)=userDao.getUserById(userId)
}