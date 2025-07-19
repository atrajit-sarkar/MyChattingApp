package com.example.mychattingapp.LocaldbLogics.DAO.daoMethods

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mychattingapp.LocaldbLogics.DAO.Entities.UserDocId
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDocIdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDocId(userDocId: UserDocId)

    // Delete a UserDoId
    @Query("DELETE FROM user_doc_id WHERE userdocid = :userdocid")
    suspend fun deleteUserDocId(userdocid: String)

    // Get all userDocIds
    @Query("SELECT * FROM user_doc_id")
    fun getAllUserDocIds(): Flow<List<UserDocId>>

}