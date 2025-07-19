package com.example.mychattingapp.LocaldbLogics.DAO.daoMethods

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.LocalMessage
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: LocalMessage)

    @Query("SELECT * FROM local_messages")
    fun getAllMessages(): Flow<List<LocalMessage>>

    @Delete
    suspend fun deleteMessages(message: LocalMessage)

    @Query("""DELETE FROM local_messages
        WHERE (sender = :uid AND receiver = :currentUserId) 
        OR (sender = :currentUserId AND receiver = :uid)
    """)

    suspend fun deleteAllMessages(uid: String, currentUserId: String)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateMessage(message: LocalMessage)

    @Query("""
        SELECT * FROM local_messages 
        WHERE (sender = :uid AND receiver = :currentUserId) 
        OR (sender = :currentUserId AND receiver = :uid)
        ORDER BY timestamp ASC
    """)
    fun getMessageById(uid: String, currentUserId: String): Flow<List<LocalMessage>>

}