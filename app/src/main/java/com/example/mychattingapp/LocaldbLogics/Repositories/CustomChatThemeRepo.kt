package com.example.mychattingapp.LocaldbLogics.Repositories

import com.example.mychattingapp.LocaldbLogics.DAO.Entities.CustomChatTheme
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.CustomChatThemeDao
import javax.inject.Inject

class CustomChatThemeRepo @Inject constructor(
    private val customChatThemeDao: CustomChatThemeDao
) {
    suspend fun insertCustomChatTheme(customChatTheme: CustomChatTheme) {
        customChatThemeDao.insertCustomChatTheme(customChatTheme)
    }

    suspend fun updateCustomChatTheme(customChatTheme: CustomChatTheme) {
        customChatThemeDao.updateCustomChatTheme(customChatTheme)
    }

    suspend fun deleteCustomChatTheme(customChatTheme: CustomChatTheme) {
        customChatThemeDao.deleteCustomChatTheme(customChatTheme)
    }

    fun getCustomChatThemeById(id: Int) = customChatThemeDao.getCustomChatThemeById(id)


    fun getAllCustomChatThemes() = customChatThemeDao.getAllCustomChatThemes()


}