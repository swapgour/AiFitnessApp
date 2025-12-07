package com.example.aifitnessapp.data.local.dao

import androidx.room.*
import com.example.aifitnessapp.domain.model.SavedDietPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface DietPlanDao {

    @Query("SELECT * FROM saved_diet_plans WHERE profileId = :id LIMIT 1")
    fun getDiet(id: Int): Flow<SavedDietPlan?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDiet(plan: SavedDietPlan)
}
