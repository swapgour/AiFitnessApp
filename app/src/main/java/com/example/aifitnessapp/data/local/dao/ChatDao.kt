package com.example.aifitnessapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.aifitnessapp.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Query("SELECT * FROM chat_messages ORDER BY id ASC")
    fun getAll(): Flow<List<ChatMessage>>

    @Insert
    suspend fun insert(msg: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clear()
}
