package com.example.aifitnessapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aifitnessapp.domain.model.SavedDietPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedDietDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlan(plan: SavedDietPlan)

    @Query("SELECT * FROM saved_diet_plans WHERE profileId = :profileId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestPlan(profileId: Int): SavedDietPlan?

    @Query("SELECT * FROM saved_diet_plans WHERE profileId = :profileId ORDER BY timestamp DESC LIMIT 1")
    fun getLatestPlanFlow(profileId: Int): Flow<SavedDietPlan?>
}
