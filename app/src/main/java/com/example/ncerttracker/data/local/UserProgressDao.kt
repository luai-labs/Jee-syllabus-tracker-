package com.example.ncerttracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ncerttracker.data.model.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {

    @Query("SELECT * FROM user_progress WHERE id = 'user_profile' LIMIT 1")
    fun getUserProgress(): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress WHERE id = 'user_profile' LIMIT 1")
    suspend fun getUserProgressOnce(): UserProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(userProgress: UserProgressEntity)

    @Query("UPDATE user_progress SET dailyGoal = :goal WHERE id = 'user_profile'")
    suspend fun updateDailyGoal(goal: Int)

    @Query("UPDATE user_progress SET lastActiveTimestamp = :timestamp WHERE id = 'user_profile'")
    suspend fun updateLastActive(timestamp: Long)
}
