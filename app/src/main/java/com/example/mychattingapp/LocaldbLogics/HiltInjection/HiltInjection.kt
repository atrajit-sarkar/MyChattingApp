package com.example.mychattingapp.LocaldbLogics.HiltInjection

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mychattingapp.LocaldbLogics.DAO.RoomDbConnection.AppDatabase
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.CustomChatThemeDao
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.MessageDao
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.SavedSettingsDao
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.UserDao
import com.example.mychattingapp.LocaldbLogics.DAO.daoMethods.UserDocIdDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val MIGRATION_X_Y = object : Migration(23, 25) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add the `userDocId` column with a default value to the `user` table
//        database.execSQL("ALTER TABLE user ADD COLUMN userDocId TEXT NOT NULL DEFAULT ''")
        // Add the `fileType` and `fileUri` columns to the `messages` table

        // Add the `fileType` and `fileUri` columns to the `local_messages` table
        database.execSQL("ALTER TABLE local_messages ADD COLUMN fileType TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE local_messages ADD COLUMN fileUri TEXT NOT NULL DEFAULT ''")

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS messages (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                chatId INTEGER NOT NULL,
                messageId TEXT NOT NULL DEFAULT '',
                sender TEXT NOT NULL,
                receiver TEXT NOT NULL DEFAULT '',
                text TEXT NOT NULL,
                timestamp TEXT NOT NULL,
                reaction TEXT NOT NULL DEFAULT '',
                icons TEXT NOT NULL DEFAULT '',
                password TEXT NOT NULL DEFAULT '',
                viewOnce TEXT NOT NULL DEFAULT '',
                editIcon TEXT NOT NULL DEFAULT '',
                deletedForMe TEXT NOT NULL DEFAULT '',
                deletedForEveryone TEXT NOT NULL DEFAULT '',
                fileType TEXT NOT NULL DEFAULT '',
                fileUri TEXT NOT NULL DEFAULT '',
                FOREIGN KEY(chatId) REFERENCES user(id) ON DELETE CASCADE
            )
            """.trimIndent()
        )
        // Add index for chatId (if required)
        database.execSQL("CREATE INDEX IF NOT EXISTS index_messages_chatId ON messages(chatId)")

    }
}


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).addMigrations(MIGRATION_X_Y)
            .build()
    }

    @Provides
    fun provideMessageDao(database: AppDatabase): MessageDao {
        return database.messageDao()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideUserDocIdDao(database: AppDatabase): UserDocIdDao {
        return database.userDocIdDao()
    }

    @Provides
    fun provideSavedSettingsDao(database: AppDatabase): SavedSettingsDao {
        return database.savedSettingsDao()
    }

    @Provides
    fun provideCustomChatThemeDao(database: AppDatabase): CustomChatThemeDao {
        return database.customChatThemeDao()
    }



}