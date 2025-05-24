package com.brycepillwein.task8_2hd.database

import androidx.room.*
import com.brycepillwein.task8_2hd.database.QuizResultEntity

@Dao
interface QuizResultDao {
  @Insert
  suspend fun insertResult(result: QuizResultEntity)

  @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
  suspend fun getAllResults(): List<QuizResultEntity>
}
