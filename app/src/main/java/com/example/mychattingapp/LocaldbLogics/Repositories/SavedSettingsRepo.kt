package com.example.mychattingapp.LocaldbLogics.Repositories

import com.example.mychattingapp.LocaldbLogics.DAO.Entities.SavedSettings
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.SavedSettingsDao
import com.example.mychattingapp.ui.ChatAPPTheme.ChatScreenTheme.ChatScreenThemeNames
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SavedSettingsRepo @Inject constructor(private val savedSettingsDao: SavedSettingsDao) {
    suspend fun insertSavedSettings() {
        val savedSettings = savedSettingsDao.getSettings().firstOrNull()
        if (savedSettings == null) {
            savedSettingsDao.insertSavedSettings(SavedSettings(
                chatTheme = ChatScreenThemeNames.Default.name,
            ))
        }
    }

    suspend fun updateSavedSettings(savedSettings: SavedSettings) {
        savedSettingsDao.updateSavedSettings(savedSettings)
    }

    fun getSettings() = savedSettingsDao.getSettings()
}