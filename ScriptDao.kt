package com.xtremeclicker.pro.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScriptDao {
    @Query("SELECT * FROM saved_scripts ORDER BY createdAt DESC")
    fun getAllScripts(): Flow<List<SavedScript>>

    @Query("SELECT * FROM saved_scripts WHERE id = :id")
    suspend fun getScriptById(id: Long): SavedScript?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(script: SavedScript): Long

    @Update
    suspend fun update(script: SavedScript)

    @Delete
    suspend fun delete(script: SavedScript)

    @Query("DELETE FROM saved_scripts WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM saved_scripts")
    suspend fun getCount(): Int
}
