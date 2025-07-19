package com.example.mychattingapp.LocaldbLogics.DAO.daoMethods

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.CustomChatTheme
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomChatThemeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomChatTheme(customChatTheme: CustomChatTheme)

    // Get all custom ChatThemes
    @Query("SELECT * FROM custom_chat_theme")
    fun getAllCustomChatThemes(): Flow<List<CustomChatTheme>>

    //Update a customChatTheme
    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateCustomChatTheme(customChatTheme: CustomChatTheme)

    //Delete a customChatTheme
    @Delete
    suspend fun deleteCustomChatTheme(customChatTheme: CustomChatTheme)

    // Give an perticular customChatTheme where id is equal to the id passed
    @Query("SELECT * FROM custom_chat_theme WHERE id = :id")
    fun getCustomChatThemeById(id: Int): Flow<CustomChatTheme>


}