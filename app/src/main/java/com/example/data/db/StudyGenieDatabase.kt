package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SavedSolutionEntity::class,
        StudyNoteEntity::class,
        CustomFlashcardEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class StudyGenieDatabase : RoomDatabase() {
    abstract fun savedSolutionDao(): SavedSolutionDao
    abstract fun studyNoteDao(): StudyNoteDao
    abstract fun customFlashcardDao(): CustomFlashcardDao

    companion object {
        @Volatile
        private var INSTANCE: StudyGenieDatabase? = null

        fun getInstance(context: Context): StudyGenieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudyGenieDatabase::class.java,
                    "study_genie.db"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
