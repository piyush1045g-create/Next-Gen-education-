package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "custom_flashcards")
data class CustomFlashcardEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val deckTitle: String,
    val subject: String,
    val question: String,
    val answer: String,
    val hint: String = "",
    val isMastered: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface CustomFlashcardDao {
    @Query("SELECT * FROM custom_flashcards ORDER BY createdAt DESC")
    fun getAllCards(): Flow<List<CustomFlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CustomFlashcardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CustomFlashcardEntity>)

    @Delete
    suspend fun deleteCard(card: CustomFlashcardEntity)

    @Query("UPDATE custom_flashcards SET isMastered = :isMastered WHERE id = :id")
    suspend fun updateMastery(id: String, isMastered: Boolean)

    @Query("DELETE FROM custom_flashcards WHERE deckTitle = :deckTitle")
    suspend fun deleteDeck(deckTitle: String)
}
