package com.example.ncerttracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ncerttracker.data.model.RevisionSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RevisionSessionDao {
    @Query("SELECT * FROM revision_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<RevisionSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: RevisionSessionEntity): Long

    @Delete
    suspend fun deleteSession(session: RevisionSessionEntity)

    @Query("DELETE FROM revision_sessions")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM revision_sessions")
    suspend fun getCount(): Int

    @Query("SELECT SUM(durationMinutes) FROM revision_sessions")
    fun getTotalRevisionMinutes(): Flow<Int?>
}
