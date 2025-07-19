package com.example.mychattingapp.LocaldbLogics.DAO.daoMethods

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.SavedSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedSettings(savedSettings: SavedSettings)

    // Update a SavedSettings
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateSavedSettings(savedSettings: SavedSettings)

    @Query("SELECT * FROM saved_settings WHERE id = 1")
    fun getSettings(): Flow<SavedSettings?>

}