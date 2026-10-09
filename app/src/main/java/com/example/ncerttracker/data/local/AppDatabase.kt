package com.example.ncerttracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ncerttracker.data.model.ChapterEntity
import com.example.ncerttracker.data.model.RevisionSessionEntity
import com.example.ncerttracker.data.model.SubtopicEntity
import com.example.ncerttracker.data.model.UserProgressEntity

@Database(
    entities = [
        ChapterEntity::class,
        SubtopicEntity::class,
        UserProgressEntity::class,
        RevisionSessionEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun chapterDao(): ChapterDao
    abstract fun subtopicDao(): SubtopicDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun revisionSessionDao(): RevisionSessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `revision_sessions` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `timestamp` INTEGER NOT NULL,
                        `durationMinutes` INTEGER NOT NULL,
                        `chapterIds` TEXT NOT NULL,
                        `topicNames` TEXT NOT NULL,
                        `subject` TEXT NOT NULL,
                        `notes` TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jee_syllabus_tracker.db"
                )
                    .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
