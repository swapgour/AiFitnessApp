package com.example.aifitnessapp.data.local.dao

import androidx.room.*
import com.example.aifitnessapp.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    // RETURN generated ID ✔ FIXED
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile): Long

    @Query("SELECT * FROM profiles")
    fun getAllProfiles(): Flow<List<UserProfile>>

    @Delete
    suspend fun deleteProfile(profile: UserProfile)

    @Query("SELECT * FROM profiles WHERE id = :id LIMIT 1")
    suspend fun getProfileById(id: Int): UserProfile?

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Query("SELECT * FROM profiles WHERE id = :id LIMIT 1")
    fun observeProfile(id: Int): Flow<UserProfile?>
}
