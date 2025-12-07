package com.example.aifitnessapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aifitnessapp.domain.model.WeightHistory

@Dao
interface WeightHistoryDao {

    // Insert a new weight entry
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weight: WeightHistory)

    // Get full weight history for a profile (sorted by date)
    @Query("SELECT * FROM weight_history WHERE profileId = :profileId ORDER BY date ASC")
    suspend fun getHistory(profileId: Int): List<WeightHistory>

    // Get latest weight entry
    @Query("SELECT * FROM weight_history WHERE profileId = :profileId ORDER BY date DESC LIMIT 1")
    suspend fun getLatest(profileId: Int): WeightHistory?
}
