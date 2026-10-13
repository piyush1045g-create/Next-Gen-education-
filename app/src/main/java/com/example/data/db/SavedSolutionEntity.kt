package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "saved_solutions")
data class SavedSolutionEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val question: String,
    val subject: String,
    val mode: String,
    val answer: String,
    val stepsJson: String = "",
    val formulasJson: String = "",
    val proTip: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

@Dao
interface SavedSolutionDao {
    @Query("SELECT * FROM saved_solutions ORDER BY timestamp DESC")
    fun getAllSolutions(): Flow<List<SavedSolutionEntity>>

    @Query("SELECT * FROM saved_solutions WHERE subject = :subject ORDER BY timestamp DESC")
    fun getSolutionsBySubject(subject: String): Flow<List<SavedSolutionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSolution(solution: SavedSolutionEntity)

    @Delete
    suspend fun deleteSolution(solution: SavedSolutionEntity)

    @Query("UPDATE saved_solutions SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("DELETE FROM saved_solutions WHERE id = :id")
    suspend fun deleteById(id: String)
}
